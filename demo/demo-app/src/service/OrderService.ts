import Order from "../model/order.model";
import { ORDER_ENDPOINT } from "../constants/api";

export class OrderService {
    static async getOrders(): Promise<Order[]> {
        const response = await fetch(ORDER_ENDPOINT);
        if (!response.ok) {
            throw new Error("Failed to fetch orders");
        }
        return response.json();
    }

    static async addOrder(order: Order): Promise<Order> {
        //  Check quantity is within available stock
        const errors: string[] = [];
        for (const item of order.motorcycles) {
            if (item.quantity > item.motorcycle.availableQuantity) {
                errors.push(
                    `${item.motorcycle.brand} ${item.motorcycle.model} has only ${item.motorcycle.availableQuantity} left`
                );
            }
        }

        if (errors.length > 0) {
            throw new Error("Order failed: \n" + errors.join("\n"));
        }

        const payload: {
            personId: string;
            date: string;
            motorcycles: { motorcycleId: string; quantity: number }[];

        } = {
            personId: order.person.id,
            date: order.date,
            motorcycles: order.motorcycles
                .filter((item) => item.quantity > 0)
                .map((item) => ({
                    motorcycleId: item.motorcycle.id,
                    quantity: item.quantity,
                })),
        };

        const response = await fetch(ORDER_ENDPOINT, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const contentType = response.headers.get("content-type");
            let errorMessage = "❌ Failed to add order.";

            if (contentType && contentType.includes("application/json")) {
                const errorJson = await response.json();
                if (typeof errorJson === "string") {
                    errorMessage = errorJson;
                } else if (typeof errorJson === "object") {
                    errorMessage = Object.values(errorJson).join("\n");
                }
            } else {
                errorMessage = await response.text();
            }

            console.error("❌ Backend returned:", errorMessage);
            throw new Error(errorMessage);
        }


        return response.json();
    }

    static async updateOrder(order: Order): Promise<void> {
        const payload: {
            id: string;
            personId: string;
            date: string;
            motorcycles: { motorcycleId: string; quantity: number }[];
        } = {
            id: order.id,
            personId: order.person.id,
            date: order.date,
            motorcycles: order.motorcycles
                .filter((item) => item.quantity > 0)
                .map((item) => ({
                    motorcycleId: item.motorcycle.id,
                    quantity: item.quantity,
                })),
        };

        const response = await fetch(`${ORDER_ENDPOINT}/${order.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("❌ Error updating order:", errorText);
            throw new Error("Failed to update order");
        }
    }

    static async deleteOrder(id: string): Promise<void> {
        const response = await fetch(`${ORDER_ENDPOINT}/${id}`, {
            method: "DELETE",
        });

        if (!response.ok) {
            throw new Error("Failed to delete order");
        }
    }

}
