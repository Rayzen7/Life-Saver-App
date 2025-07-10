/* eslint-disable prettier/prettier */
import { Module } from "@nestjs/common";
import { DoctorEntity } from "./doctor.entity";
import { DoctorController } from "./doctor.controller";
import { DoctorService } from "./doctor.service";
import { TypeOrmModule } from "@nestjs/typeorm";

@Module({
  imports: [TypeOrmModule.forFeature([DoctorEntity])],
  controllers: [DoctorController],
  providers: [DoctorService],
  exports: [],
})

export class DoctorModule {}