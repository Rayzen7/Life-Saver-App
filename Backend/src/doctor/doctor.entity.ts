/* eslint-disable prettier/prettier */
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn } from "typeorm";

@Entity('doctor')
export class DoctorEntity {
    @PrimaryGeneratedColumn()
    id: number

    @Column()
    name: string

    @Column('text')
    desc: string

    @Column('bigint')
    no_phone: number

    @Column('bigint')
    price: number

    @CreateDateColumn()
    created_at: Date

    @UpdateDateColumn()
    updated_at: Date
}