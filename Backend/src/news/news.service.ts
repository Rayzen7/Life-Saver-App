/* eslint-disable prettier/prettier */
import { Injectable, HttpException } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { Repository } from "typeorm";
import { NewsEntity } from "./news.entity";
import { NewsDto } from "./news.dto";
import * as fs from 'fs';
import * as path from 'path';

@Injectable()
export class NewsService {
    constructor(
        @InjectRepository(NewsEntity)
        private newsRepo: Repository<NewsEntity>
    ) {}

    async findAll(): Promise<{ news: NewsEntity[] }> {
        const news = await this.newsRepo.find({
            relations: ['user'],
        });

        return {
            news: news,
        };
    }

    async create(data: NewsDto, file: Express.Multer.File, user_id: number): Promise<{ news: NewsEntity | null, message: string }> {
        const newNews = this.newsRepo.create({
            name: data.name,
            desc: data.desc,
            image: file.filename,
            link: data.link,
            user: { id: user_id }
        });

        await this.newsRepo.save(newNews);
        const newsCreated = await this.newsRepo.findOne({
            where: { id: newNews.id },
            relations: ['user']
        });

        return {
            message: 'Create News Succss',
            news: newsCreated,
        }
    }

    async findById(id: number): Promise<{ news: NewsEntity | null }> {
        const news = await this.newsRepo.findOne({
            where: { id },
            relations: ['user'],
        });

        if (!news) {
            throw new HttpException({
                statusCode: 404,
                message: 'News Not Found',
            }, 404);
        }

        return {
            news: news,
        };
    }

    async update(id: number, data: Partial<NewsDto>, file: Express.Multer.File): Promise<{ message: string, news: NewsEntity | null }> {
        const news = await this.newsRepo.findOne({
            where: { id },
            relations: ['user']
        });

        if (!news) {
            throw new HttpException({
                statusCode: 404,
                message: 'News Not Found',
            }, 404);
        }

        if (news.image) {
            const imagePath = path.join(__dirname, '..', '..', 'uploads', 'news', news.image);
            if (fs.existsSync(imagePath)) {
                fs.unlinkSync(imagePath);
            }
        }

        const updatedData = {
            ...data,
            image: file?.filename || news.image,            
        }
        
        await this.newsRepo.update(id, updatedData);
        const updateNews = await this.newsRepo.findOne({
            where: { id },
            relations: ['user']
        });

        return {
            message: 'Update News Success',
            news: updateNews,
        }
    }

    async destroy(id: number): Promise<{ message: string }> {
        const news = await this.newsRepo.findOne({
            where: { id },
            relations: ['user']
        });

        if (!news) {
            throw new HttpException({
                statusCode: 404,
                message: 'News Not Found',
            }, 404);
        }

        if (news.image) {
            const imagePath = path.join(__dirname, '..', '..', 'uploads', 'news', news.image);
            if (fs.existsSync(imagePath)) {
                fs.unlinkSync(imagePath);
            }
        }

        await this.newsRepo.remove(news);
        return {
            message: 'Delete News Success'
        }
    }
}