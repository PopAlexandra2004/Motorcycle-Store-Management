// services/PersonService.ts
import Person from '../model/person.model';
import { PERSON_ENDPOINT } from '../constants/api';

async function getErrorMessage(response: Response): Promise<string> {
    const contentType = response.headers.get("content-type");

    if (contentType?.includes("application/json")) {
        const data = await response.json();
        if (typeof data === "object" && data !== null) {
            return Object.values(data).join("\n");
        }
        if (typeof data === "string") {
            return data;
        }
    }

    return await response.text();
}




export class PersonService {
    static async getPersons(): Promise<Person[]> {
        const response = await fetch(PERSON_ENDPOINT);
        if (!response.ok) {
            throw new Error('Failed to fetch persons');
        }
        return response.json();
    }

    static async addPerson(person: Omit<Person, 'id'>): Promise<Person> {
        const response = await fetch(PERSON_ENDPOINT, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(person),
        });
        if (!response.ok) {
            const msg = await getErrorMessage(response);
            console.error("🛑 Backend error:", msg);
            throw new Error(msg || '❌ Failed to add person.');
        }
        return response.json();
    }

    static async updatePerson(person: Person): Promise<void> {
        const response = await fetch(`${PERSON_ENDPOINT}/${person.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(person),
        });
        if (!response.ok) {
            const msg = await getErrorMessage(response);
            console.error("🛑 Backend error (update):", msg);
            throw new Error(msg || '❌ Failed to update person.');
        }
    }

    static async deletePerson(id: string): Promise<void> {
        const response = await fetch(`${PERSON_ENDPOINT}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Failed to delete person');
        }
    }
}