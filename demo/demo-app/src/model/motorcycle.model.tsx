interface Motorcycle {
    id: string;
    brand: string;
    model: string;
    year: number;
    price: number;
    engineCapacity: number; // in cubic centimeters (cc)
    availableQuantity: number; // ✅ new field

    color?: string;         // optional field
}

export default Motorcycle;
