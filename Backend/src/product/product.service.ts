/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Repository } from "typeorm";
import { ProductDto } from "./product.dto";
import { ProductEntity } from "./product.entity";
import * as fs from  'fs'
import * as path from 'path';

@Injectable()
export class ProductService {
    constructor(
        @InjectRepository(ProductEntity)
        private productRepo: Repository<ProductEntity>
    ) {}

    async findAll(): Promise<{ product: ProductEntity[] }> {
        const product = await this.productRepo.find({
            relations: ['user', 'category']
        });

        return {
            product: product
        }
    }

    async create(data: ProductDto, user_id: number, file: Express.Multer.File): Promise<{ message: string, product: ProductEntity }> {
        const newProduct = this.productRepo.create({
            user_id: user_id,
            category_id: data.category_id,
            name: data.name,
            desc: data.desc,
            dosis: data.dosis,
            image: file.filename,
            quantity: data.quantity,
            price: data.price
        });

        const product = await this.productRepo.save(newProduct);
        return {
            message: 'Create Product Success',
            product: product,
        }
    }

    async findOne(id: number): Promise<{ product: ProductEntity }> {
        const product = await this.productRepo.findOne({
            where: { id: id },
            relations: ['user', 'category']
        });

        if (!product) {
            throw new HttpException({
                statusCode: 404,
                message: 'Product Not Found',
            }, 404);
        }

        return {
            product: product
        }
    }

    async update(id: number, data: Partial<ProductDto>, user_id: number, file: Express.Multer.File): Promise<{ message: string, product: ProductEntity | null }> {
        const product = await this.productRepo.findOne({
            where: { id: id },
            relations: ['user', 'category']
        });

        if (!product) {
            throw new HttpException({
                statusCode: 404,
                message: 'Product Not Found',
            }, 404);
        }

        if (product.image) {
            const imagePath = path.join(__dirname, '..', '..', 'uploads', 'product', product.image);
            if (fs.existsSync(imagePath)) {
                fs.unlinkSync(imagePath);
            }
        }

        await this.productRepo.update(id, {
            user_id: user_id,
            category_id: data.category_id,
            name: data.name,
            image: file?.filename || product.image,
            desc: data.desc,
            dosis: data.dosis,
            quantity: data.quantity,
            price: data.quantity
        });

        const newProduct = await this.productRepo.findOne({
            where: { id: id },
            relations: ['user', 'category']
        });

        return {
            message: "Updated Product Success",
            product: newProduct,
        }
    }

    async destroy(id: number): Promise<{ message: string }> {
        const product = await this.productRepo.findOne({
            where: { id: id },
            relations: ['user', 'category']
        });

        if (!product) {
            throw new HttpException({
                statusCode: 404,
                message: 'Product Not Found',
            }, 404);
        }

        if (product.image) {
            const imagePath = path.join(__dirname, '..', '..', 'uploads', 'product', product.image);
            if (fs.existsSync(imagePath)) {
                fs.unlinkSync(imagePath);
            }
        }

        await this.productRepo.remove(product);
        return {
            message: 'Delete Product Success',
        }
    }
}