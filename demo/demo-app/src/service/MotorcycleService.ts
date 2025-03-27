// services/MotorcycleService.ts
import Motorcycle from '../model/motorcycle.model';
import { MOTORCYCLE_ENDPOINT } from '../constants/api';

async function getErrorMessage(response: Response): Promise<string> {
    const contentType = response.headers.get("content-type");

    if (contentType?.includes("application/json")) {
        const data = await response.json();
        if (typeof data === "object") return Object.values(data).join("\n");
        if (typeof data === "string") return data;
    }

    return await response.text();
}

export class MotorcycleService {
    static async getMotorcycles(): Promise<Motorcycle[]> {
        const response = await fetch(MOTORCYCLE_ENDPOINT);
        if (!response.ok) {
            throw new Error('Failed to fetch motorcycles');
        }
        return response.json();
    }

    static async addMotorcycle(motorcycle: Omit<Motorcycle, 'id'>): Promise<Motorcycle> {
        const response = await fetch(MOTORCYCLE_ENDPOINT, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(motorcycle),
        });
        if (!response.ok) {
            const msg = await getErrorMessage(response);
            console.error("🛑 Backend motorcycle add error:", msg);
            throw new Error(msg || '❌ Failed to add motorcycle.');
        }
        return response.json();
    }

    static async updateMotorcycle(motorcycle: Motorcycle): Promise<void> {
        const response = await fetch(`${MOTORCYCLE_ENDPOINT}/${motorcycle.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(motorcycle),
        });
        if (!response.ok) {
            const msg = await getErrorMessage(response);
            console.error("🛑 Backend motorcycle update error:", msg);
            throw new Error(msg || '❌ Failed to update motorcycle.');
        }
    }

    static async deleteMotorcycle(id: string): Promise<void> {
        const response = await fetch(`${MOTORCYCLE_ENDPOINT}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            const msg = await getErrorMessage(response);
            console.error("🛑 Backend motorcycle delete error:", msg);
            throw new Error(msg || '❌ Failed to delete motorcycle.');
        }
    }

}