/* eslint-disable prettier/prettier */
import { DataSource } from "typeorm";
import { join } from "path";

export const databaseProvider = [
    {
        provide: 'DATA_SOURCE',
        useFactory: async() => {
            const dataSource = new DataSource({
                type: 'mysql',
                host: 'localhost',
                port: 3306,
                username: 'root',
                password: '',
                database: 'lifesaver',
                entities: [join(__dirname, '/../**/*.entity.{ts,js}')],
                synchronize: true
            });

            return dataSource.initialize();
        }
    }
];