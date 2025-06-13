/* eslint-disable prettier/prettier */
import {  Controller,Body, Get, Put, Param, Post, Delete, UseGuards } from "@nestjs/common";
// import { UseGuards } from "@nestjs/common";
import { CategoryService } from "./category.service";
import { CategoryDto } from "./category.dto";
import { JwtAuthGuard } from "src/auth/jwt.guard";

@Controller('/category')
export class CategoryController {
    constructor(
        private readonly categoryService: CategoryService,
    ) {}

    @Get()
    @UseGuards(JwtAuthGuard)
    async findAll() {
        return this.categoryService.findAll();
    }

    @Post()
    @UseGuards(JwtAuthGuard)
    async create(@Body() data: CategoryDto) {
        const category = await this.categoryService.create(data);
        return category
    }

    @Get('/:id')
    @UseGuards(JwtAuthGuard)
    async findOne(@Param('id') id: number) {
        const category = await this.categoryService.findOne(id);
        return category;
    }

    @Put('/:id')
    @UseGuards(JwtAuthGuard)
    async update(@Param('id') id: number, @Body() data: Partial<CategoryDto>) {
        const category = await this.categoryService.update(id, data);
        return category;
    }

    @Delete('/:id')
    @UseGuards(JwtAuthGuard)
    async destroy(@Param('id') id: number) {
        const category = await this.categoryService.destroy(id);
        return category;
    }
}