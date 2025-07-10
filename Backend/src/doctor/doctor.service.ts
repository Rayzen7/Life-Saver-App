/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { DoctorEntity } from "./doctor.entity";
import { DoctorDto } from "./doctor.dto";
import { Repository } from "typeorm";

@Injectable()
export class DoctorService {
    constructor(
        @InjectRepository(DoctorEntity)
        private doctorRepo: Repository<DoctorEntity>
    ){}

    async findAll(): Promise<{ doctor: DoctorEntity[] }> {
        const doctor = await this.doctorRepo.find();
        return {
            doctor: doctor
        }
    }

    async create(data: DoctorDto): Promise<{ message: string, doctor: DoctorDto | null }> {
        const newDoctor = this.doctorRepo.create({
            name: data.name,
            desc: data.desc,
            no_phone: data.no_phone,
            price: data.price
        });

        const doctor = await this.doctorRepo.save(newDoctor);
        return {
            message: "Create News Success",
            doctor: doctor
        }
    }

    async findOne(id: number): Promise<{ doctor: DoctorDto | null }> {
        const doctor = await this.doctorRepo.findOne({
            where: { id }
        });

        if (!doctor) {
            throw new HttpException({
                statusCode: 404,
                message: 'Doctor Not Found'
            }, 404)
        }

        return {
            doctor: doctor
        }
    }

    async update(id: number, data: Partial<DoctorDto>): Promise<{ doctor: DoctorDto | null, message: string }> {
        const doctor = await this.doctorRepo.findOne({
            where: { id }
        });

        if (!doctor) {
            throw new HttpException({
                statusCode: 404,
                message: 'Doctor Not Found'
            }, 404)
        }

        await this.doctorRepo.update(id, {
            name: data.name,
            desc: data.desc,
            no_phone: data.no_phone,
            price: data.price
        })

        const newDoctor = await this.doctorRepo.findOne({
            where: { id: id }
        })
        
        return {
            message: 'Update Doctor Success',
            doctor: newDoctor
        }
    }

    async delete(id: number): Promise<{ message: string }> {
        const doctor = await this.doctorRepo.findOne({
            where: { id }
        });

        if (!doctor) {
            throw new HttpException({
                statusCode: 404,
                message: 'Doctor Not Found'
            }, 404)
        }

        await this.doctorRepo.delete(id)
        return {
            message: 'Delete Doctor Success'
        }
    }
}