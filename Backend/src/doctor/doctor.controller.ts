/* eslint-disable prettier/prettier */
import { Controller, Get, Param, Post, Put, Delete, Body, UseGuards } from "@nestjs/common";
import { DoctorService } from "./doctor.service";
import { DoctorDto } from "./doctor.dto";
import { JwtAuthGuard } from "src/auth/jwt.guard";

@Controller('/doctor')
export class DoctorController {
    constructor(
        private readonly doctorService: DoctorService
    ) {}

    @Get()
    async findAll() {
        const doctor = await this.doctorService.findAll();
        return doctor;
    }

    @Post()
    @UseGuards(JwtAuthGuard)
    async create(@Body() data: DoctorDto) {
        const createDoctor = await this.doctorService.create(data);
        return createDoctor;
    }

    @Get('/:id')
    async findOne(@Param('id') id: number) {
        const doctor = await this.doctorService.findOne(id);
        return doctor;
    }

    @Put('/:id')
    @UseGuards(JwtAuthGuard)
    async update(@Param('id') id: number, @Body() data: Partial<DoctorDto>) {
        const updateDoctor = await this.doctorService.update(id, data);
        return updateDoctor;
    }

    @Delete('/:id')
    @UseGuards(JwtAuthGuard)
    async delete(@Param('id') id: number) {
        const deleteDoctor = await this.doctorService.delete(id);
        return deleteDoctor;
    }
}