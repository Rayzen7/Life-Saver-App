/* eslint-disable prettier/prettier */
import { IsNotEmpty } from "class-validator";

export class DoctorDto {
    @IsNotEmpty()
    name: string

    @IsNotEmpty()
    desc: string

    @IsNotEmpty()
    no_phone: number

    @IsNotEmpty()
    price: number
}