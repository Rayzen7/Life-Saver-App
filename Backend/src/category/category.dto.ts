/* eslint-disable prettier/prettier */
import { IsNotEmpty } from "class-validator";

export class CategoryDto {
    @IsNotEmpty()
    name: string;
}