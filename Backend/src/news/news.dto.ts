/* eslint-disable prettier/prettier */
import { IsNotEmpty } from "class-validator";

export class NewsDto {
    @IsNotEmpty()
    name: string;

    @IsNotEmpty()
    desc: string;

    @IsNotEmpty()
    link: string;
}