/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { Repository } from "typeorm";
import { InjectRepository } from "@nestjs/typeorm";
import { TransactionEntity } from "./transaction.entity";
import { TransactionDto } from "./transaction.dto";

@Injectable()
export class TransactionService {
    constructor(
        @InjectRepository(TransactionEntity)
        private transactionRepo: Repository<TransactionEntity>
    ) {}

    async findAll(): Promise<{ transaction: TransactionEntity[] }> {
        const transaction = await this.transactionRepo.find({
            relations: ['user', 'product']
        });

        return {
            transaction: transaction,
        }
    }

    async create(data: TransactionDto, user_id: number): Promise<{ message: string, transaction: TransactionEntity | null }> {
        const newTransaction = this.transactionRepo.create({
            user_id: user_id,
            product_id: data.product_id,
            status: data.status,
            information: data.information,
            quantity: data.quantity,
            total: data.total,
        });

        const transaction = await this.transactionRepo.save(newTransaction);
        return {
            message: 'Create Transaction Success',
            transaction: transaction,
        }
    }

    async findOne(id: number): Promise<{ transaction: TransactionEntity | null }> {
        const transaction = await this.transactionRepo.findOne({
            relations: ['user', 'product'],
            where: { id: id }
        });

        if (!transaction) {
            throw new HttpException({
                statusCode: 404,
                message: "Transaction Not Found",
            }, 404);
        }

        return {
            transaction: transaction,
        }
    }

    async update(id: number, data: Partial<TransactionDto>, user_id: number): Promise<{ message: string, transaction: TransactionEntity | null }> {
        const transaction = await this.transactionRepo.findOne({
            relations: ['user', 'product'],
            where: { id: id }
        });

        if (!transaction) {
            throw new HttpException({
                statusCode: 404,
                message: "Transaction Not Found",
            }, 404);
        }

        await this.transactionRepo.update(id, {
            user_id: user_id,
            product_id: data.product_id,
            status: data.status,
            information: data.information,
            quantity: data.quantity,
            total: data.total
        });

        const transactionUpdated = await this.transactionRepo.findOne({
            relations: ['user', 'product'],
            where: { id: id }
        });

        return {
            message: 'Update Transaction Success',
            transaction: transactionUpdated,
        }
    }

    async destroy(id: number): Promise<{ message: string }> {
        const transaction = await this.transactionRepo.findOne({
            relations: ['user', 'product'],
            where: { id: id }
        });

        if (!transaction) {
            throw new HttpException({
                statusCode: 404,
                message: "Transaction Not Found",
            }, 404);
        }

        await this.transactionRepo.remove(transaction);
        return {
            message: 'Delete Transaction Success'
        }
    }
}