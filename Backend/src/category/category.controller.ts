/* eslint-disable prettier/prettier */
import {  Controller,Body, Get, Put, Param, Post, Delete } from "@nestjs/common";
// import { UseGuards } from "@nestjs/common";
import { CategoryService } from "./category.service";
import { CategoryDto } from "./category.dto";

@Controller('/category')
export class CategoryController {
    constructor(
        private readonly categoryService: CategoryService,
    ) {}

    @Get()
    async findAll() {
        return this.categoryService.findAll();
    }

    @Post()
    async create(@Body() data: CategoryDto) {
        const category = await this.categoryService.create(data);
        return category
    }

    @Get('/:id')
    async findOne(@Param('id') id: number) {
        const category = await this.categoryService.findOne(id);
        return category;
    }

    @Put('/:id')
    async update(@Param('id') id: number, @Body() data: Partial<CategoryDto>) {
        const category = await this.categoryService.update(id, data);
        return category;
    }

    @Delete('/:id')
    async destroy(@Param('id') id: number) {
        const category = await this.categoryService.destroy(id);
        return category;
    }
}