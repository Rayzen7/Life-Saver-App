/* eslint-disable prettier/prettier */
import { IsNotEmpty } from "class-validator";

export class ProductDto {
    @IsNotEmpty()
    category_id: number;

    @IsNotEmpty()
    name: string;

    @IsNotEmpty()
    desc: string;

    @IsNotEmpty()
    dosis: string;

    @IsNotEmpty()
    quantity: number;

    @IsNotEmpty()
    price: number;
}