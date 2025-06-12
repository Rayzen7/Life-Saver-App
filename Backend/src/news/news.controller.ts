/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Controller, Body, Get, Post, Param, Delete, UploadedFile, UseInterceptors, Req, UseGuards, Put } from "@nestjs/common";
import { NewsService } from "./news.service";
import { FileInterceptor } from "@nestjs/platform-express";
import { Express } from "express";
import { NewsDto } from "./news.dto";
import { diskStorage } from "multer";
import { JwtAuthGuard } from "src/auth/jwt.guard";

@Controller('/news')
export class NewsController {
    constructor(
        private readonly newsService: NewsService,
    ) {}

    @Get()
    async findAll() {
        return this.newsService.findAll();
    }

    @Post()
    @UseGuards(JwtAuthGuard)
    @UseInterceptors(FileInterceptor('image', {
        storage: diskStorage({
            destination: './uploads/news',
            filename: (req, file, callback) => {
                const timestamp = Date.now();
                const originalName = file.originalname.replace(/\s+/g, '-');
                callback(null, `${timestamp}-${originalName}`);
            }
        })
    }))
    async create(@Body() data: NewsDto, @UploadedFile() file: Express.Multer.File, @Req() req: any) {
        const user = req.user;
        return this.newsService.create(data, file, user.id);
    }

    @Get('/:id')
    async findOne(@Param('id') id: number) {
        return this.newsService.findOne(id);
    }

    @UseInterceptors(FileInterceptor('image', {
        storage: diskStorage({
            destination: './uploads/news',
            filename: (req, file, callback) => {
                const timestamp = Date.now();
                const originalName = file.originalname.replace(/\s+/g, '-');
                callback(null, `${timestamp}-${originalName}`);
            }
        })
    }))
    @Put('/:id')
    @UseGuards(JwtAuthGuard)
    async update(@Param('id') id: number, @Body() data: Partial<NewsDto>, @UploadedFile() file: Express.Multer.File) {
        return this.newsService.update(id, data, file);
    }

    @Delete('/:id')
    @UseGuards(JwtAuthGuard)
    async remove(@Param('/:id') id: number) {
        return this.newsService.destroy(id);
    }
}