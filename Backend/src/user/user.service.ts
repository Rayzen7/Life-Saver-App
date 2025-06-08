/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Repository } from "typeorm";
import { User } from "./user.entity";
import { UserDto } from "./user.dto";
import * as bcrypt from 'bcryptjs';

@Injectable()
export class UserService {
    constructor(
        @InjectRepository(User)
        private userRepo: Repository<User>,        
    ) {}

    async create(data: UserDto): Promise<User | undefined> {
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
            password: hashedPassword,
            nik: data.nik,
            phone: data.phone,
            role: data.role
        });

        const savedUser = await this.userRepo.save(newUser);
        return savedUser;
    }

    async findAll(): Promise<User[]> {
        const user = await this.userRepo.find();
        return user;
    }

    async findOne(id: number): Promise<User | null> {
        const user = await this.userRepo.findOneBy({ id });
        return user;
    }

    async update(id: number, data: Partial<UserDto>): Promise<User | null> {
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

        if (data.password) {
            data.password = await bcrypt.hash(data.password, 10);
        }

        await this.userRepo.update(id, data);
        const user = this.userRepo.findOneBy({ id });
        return user;
    }

    async remove(id: number): Promise<void> {
        await this.userRepo.delete(id);
    }

    async findByEmail(email: string): Promise<User | null> {
        const user = await this.userRepo.findOne({
            where: [{ email }]
        });

        return user;
    }
}