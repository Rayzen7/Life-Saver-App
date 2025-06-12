/* eslint-disable prettier/prettier */
import { Module } from "@nestjs/common";
import { CategoryEntity } from "./category.entity";
import { CategoryService } from "./category.service";
import { CategoryController } from "./category.controller";
import { TypeOrmModule } from "@nestjs/typeorm";

@Module({
  imports: [TypeOrmModule.forFeature([CategoryEntity])],
  controllers: [CategoryController],
  providers: [CategoryService],
})

export class CategoryModule {}