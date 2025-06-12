/* eslint-disable prettier/prettier */
import { CategoryEntity } from "./category.entity";
import { Injectable, HttpException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Repository } from "typeorm";
import { CategoryDto } from "./category.dto";

@Injectable()
export class CategoryService {
    constructor(
        @InjectRepository(CategoryEntity)
        private categoryRepo: Repository<CategoryEntity>
    ) {}

    async findAll(): Promise<{ category: CategoryEntity[] }> {
        const category = await this.categoryRepo.find();
        return {
            category: category,
        }
    }

    async create(data: CategoryDto): Promise<{ category: CategoryEntity | null, message: string }> {
        const newCategory = this.categoryRepo.create({
            name: data.name,
        });

        const category = await this.categoryRepo.save(newCategory);
        return {
            message: "Create Category Success",
            category: category,
        }
    }

    async findOne(id: number): Promise<{ category: CategoryEntity | null }> {
        const category = await this.categoryRepo.findOne({
            where: { id: id }
        });

        if (!category) {
            throw new HttpException({
                statusCode: 404,
                message: 'Category Not Found'
            }, 404);
        }

        return {
            category: category,
        }
    }

    async update(id: number, data: Partial<CategoryDto>): Promise<{ message: string, category: CategoryEntity | null }> {
        const category = await this.categoryRepo.findOne({
            where: { id: id }
        });

        if (!category) {
            throw new HttpException({
                statusCode: 404,
                message: 'Category Not Found'
            }, 404);
        }

        await this.categoryRepo.update(id, {
            name: data.name,
        });

        const newCategory = await this.categoryRepo.findOne({
            where: { id: id }
        });

        return {
            message: 'Update Category Success',
            category: newCategory
        }
    }

    async destroy(id: number): Promise<{ message: string }> {
        const category = await this.categoryRepo.findOne({
            where: { id: id }
        });

        if (!category) {
            throw new HttpException({
                statusCode: 404,
                message: 'Category Not Found'
            }, 404);
        }

        await this.categoryRepo.remove(category);
        return {
            message: "Delete Category Success"
        }
    }
}