/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
import { Body, Controller, Delete, Get, HttpCode, Param, Post, Put, UseGuards } from "@nestjs/common";
import { UserService } from "./user.service";
import { UserDto } from "./user.dto";
import { JwtAuthGuard } from "src/auth/jwt.guard";

@Controller('/users')
export class UserController {
    constructor(private readonly userService: UserService) {}

    @Post()
    @HttpCode(201)
    async create(@Body() data: UserDto) {
        const user = await this.userService.create(data);
        return {
            message: 'Create User Success',
            user,
        };
    }

    @Get()
    @UseGuards(JwtAuthGuard)
    async findAll() {
        const user = await this.userService.findAll();
        return {
            user: user,
        }
    }

    @Get('/:id')
    @UseGuards(JwtAuthGuard)
    async findOne(@Param('id') id: number) {
        const user = await this.userService.findOne(id);
        return {
            user: user,
        }
    }

    @Put('/:id')
    @HttpCode(200)
    async update(@Param('id') id: number, @Body() data: Partial<UserDto>) {
        const user = await this.userService.update(id, data);
        return {
            message: "Update User Success",
            user: user,
        }
    }

    @Delete('/:id')
    @UseGuards(JwtAuthGuard)
    @HttpCode(200)
    async remove(@Param('id') id: number) {
        await this.userService.remove(id);
        return {
            message: "Delete User Success"
        }
    }
}