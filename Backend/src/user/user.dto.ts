/* eslint-disable prettier/prettier */
import { IsEmail, IsEnum, IsNotEmpty, IsOptional, MinLength, IsNumber } from "class-validator";
import { UserRole } from "./user.entity";

export class UserDto {
    @IsNotEmpty()
    username: string;
    
    @IsNotEmpty()
    @IsEmail()
    email: string;
    
    @IsNotEmpty()
    @MinLength(6)
    password: string;

    @IsNotEmpty()
    @IsNumber()
    nik: number;

    @IsNotEmpty()
    phone: string

    @IsOptional()
    @IsEnum(UserRole)
    role?: UserRole;
}