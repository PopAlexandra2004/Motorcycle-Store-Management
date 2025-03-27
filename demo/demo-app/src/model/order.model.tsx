import Person from "./person.model";
import Motorcycle from "./motorcycle.model";

export interface OrderedMotorcycle {
    motorcycle: Motorcycle;
    quantity: number;
}

interface Order {
    id: string;
    person: Person;
    motorcycles: OrderedMotorcycle[];
    date: string;        // ISO format: "YYYY-MM-DDTHH:mm:ss"
    totalCost: number;
}

export default Order;
