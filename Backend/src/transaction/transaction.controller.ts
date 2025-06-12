/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
import { Controller, Body, Get, Post, Param, Put, Delete, Req, UseGuards } from "@nestjs/common";
import { JwtAuthGuard } from "src/auth/jwt.guard";
import { TransactionService } from "./transaction.service";
import { TransactionDto } from "./transaction.dto";

@Controller('/transaction')
export class TransactionController {
    constructor(
        private readonly transactionService: TransactionService,
    ) {}

    @Get()
    @UseGuards(JwtAuthGuard)
    async findAll() {
        const transaction = await this.transactionService.findAll();
        return transaction;
    }

    @Post()
    @UseGuards(JwtAuthGuard)
    async create(@Body() data: TransactionDto, @Req() req: any) {
        const user_id = req.user.id;
        const transaction = await this.transactionService.create(data, user_id);
        return transaction;
    }


    @Get('/:id')
    @UseGuards(JwtAuthGuard)
    async findOne(@Param('id') id: number) {
        const transaction = await this.transactionService.findOne(id);
        return transaction;
    }

    @Put('/:id')
    @UseGuards(JwtAuthGuard)
    async update(@Param('id') id: number, @Body() data: Partial<TransactionDto>, @Req() req: any) {
        const user_id = req.user.id;
        const product = await this.transactionService.update(id, data, user_id)
        return product;
    }

    @Delete('/:id')
    @UseGuards(JwtAuthGuard)
    async destroy(@Param('id') id: number) {
        const product = await this.transactionService.destroy(id);
        return product;
    }
}