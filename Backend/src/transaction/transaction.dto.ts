/* eslint-disable prettier/prettier */
import { IsNotEmpty, IsOptional } from "class-validator";
import { TransactionStatus } from "./transaction.entity";

export class TransactionDto {
    @IsNotEmpty()
    product_id: number;

    @IsOptional()
    status: TransactionStatus

    @IsNotEmpty()
    information: string;

    @IsNotEmpty()
    quantity: number;

    @IsNotEmpty()
    total: number;
}