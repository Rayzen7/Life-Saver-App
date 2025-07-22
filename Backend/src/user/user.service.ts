/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Repository } from "typeorm";
import { UserEntity } from "./user.entity";
import { UserDto } from "./user.dto";
import * as bcrypt from 'bcryptjs';

@Injectable()
export class UserService {
    constructor(
        @InjectRepository(UserEntity)
        private userRepo: Repository<UserEntity>,
    ) {}

    async create(data: UserDto): Promise<{ message: string, user: UserEntity | undefined }> {
        const existingUser = await this.userRepo.findOne({
            where: [{ email: data.email }]
        });

        if (existingUser) {
            throw new HttpException(
              {
                statusCode: 422,
                message: 'Email already used',
                error: 'Unprocessable Entity',
              },
              422,
            );
        }

        const hashedPassword = await bcrypt.hash(data.password, 10);
        const newUser = this.userRepo.create({
            username: data.username,
            email: data.email,
            password: hashedPassword
        });

        const savedUser = await this.userRepo.save(newUser);
        return {
            message: 'Register Success',
            user: savedUser
        };
    }

    async findAll(): Promise<{ user: UserEntity[] }> {
        const user = await this.userRepo.find({
            relations: ['news', 'product', 'transaction']
        });
        return {
            user: user,
        };
    }

    async findOne(id: number): Promise<{ user: UserEntity | null }> {
        const user = await this.userRepo.findOne({ 
            where: { id },
            relations: ['news', 'product', 'transaction']
         });
        return {
            user: user
        };
    }

    async update(id: number, data: Partial<UserDto>): Promise<{ message: string, user: UserEntity | null }> {
        if (data.password) {
            data.password = await bcrypt.hash(data.password, 10);
        }

        await this.userRepo.update(id, data);
        const user = await this.userRepo.findOne({ 
            where: { id: id }
         });

        return {
            message: "Update User Success",
            user: user,
        };
    }

    async remove(id: number): Promise<{ message: string }> {
        await this.userRepo.delete(id);
        return {
            message: "Delete User Success"
        }
    }

    async findByEmail(email: string): Promise<UserEntity | null> {
        const user = await this.userRepo.findOne({
            where: [{ email }]
        });

        return user;
    }
}