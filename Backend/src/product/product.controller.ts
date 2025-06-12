/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Controller, Body, Get, Post, Param, Put, Delete, Req, UseGuards, UseInterceptors, UploadedFile } from "@nestjs/common";
import { JwtAuthGuard } from "src/auth/jwt.guard";
import { ProductDto } from "./product.dto";
import { ProductService } from "./product.service";
import { FileInterceptor } from "@nestjs/platform-express";
import { diskStorage } from "multer";

@Controller('/product')
export class ProductController {
    constructor(
        private readonly productService: ProductService,
    ) {}

    @Get()
    async findAll() {
        return this.productService.findAll();
    }

    @Post()
    @UseGuards(JwtAuthGuard)
    @UseInterceptors(FileInterceptor('image', {
        storage: diskStorage({
            destination: './uploads/product',
            filename: (req, file, callback) => {
                const timestamp = Date.now();
                const originalName = file.originalname.replace(/\s+/g, '');
                callback(null, `${timestamp}-${originalName}`);
            }
        })
    }))
    async create(@Body() data: ProductDto, @Req() req: any, @UploadedFile() file: Express.Multer.File) {
        const user_id = req.user.id;
        const product = await this.productService.create(data, user_id, file);
        return product;
    }

    @Get('/:id')
    async findOne(@Param('id') id: number) {
        const product = await this.productService.findOne(id);
        return product;
    }

    @Put('/:id')
    @UseGuards(JwtAuthGuard)
    @UseInterceptors(FileInterceptor('image', {
        storage: diskStorage({
            destination: './uploads/product',
            filename: (req, file, callback) => {
                const timestamp = Date.now();
                const originalName = file.originalname.replace(/\s+/g, '');
                callback(null, `${timestamp}-${originalName}`);
            }
        })
    }))
    async update(@Param('id') id: number, @Body() data: Partial<ProductDto>, @Req() req: any, @UploadedFile() file: Express.Multer.File) {
        const user_id = req.user.id;
        const product = await this.productService.update(id, data, user_id, file);
        return product;
    }

    @Delete('/:id')
    async destroy(@Param('id') id: number) {
        const product = await this.productService.destroy(id);
        return product;
    }
}