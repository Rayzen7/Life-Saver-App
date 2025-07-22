/* eslint-disable prettier/prettier */
import { IsEmail, IsEnum, IsNotEmpty, IsOptional, MinLength, IsNumber } from "class-validator";
import { UserRole } from "./user.entity";

export class UserDto {
    @IsOptional()
    id: number;

    @IsNotEmpty()
    username: string;
    
    @IsNotEmpty()
    @IsEmail()
    email: string;
    
    @IsNotEmpty()
    @MinLength(6)
    password: string;

    @IsOptional()
    @IsNumber()
    nik?: number;

    @IsOptional()
    phone?: string

    @IsOptional()
    @IsEnum(UserRole)
    role?: UserRole;
}