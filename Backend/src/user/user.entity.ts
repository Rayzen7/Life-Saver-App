/* eslint-disable prettier/prettier */
import { NewsEntity } from "src/news/news.entity";
import { Exclude } from "class-transformer";
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, OneToMany } from "typeorm";
import { ProductEntity } from "src/product/product.entity";
import { TransactionEntity } from "src/transaction/transaction.entity";

export enum UserRole {
    ADMIN = 'admin',
    USER = 'user',
    DOCTOR = 'doctor'
}

@Entity('users')
export class UserEntity {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    username: string;

    @Column({ unique: true })
    email: string;

    @Column()
    @Exclude()
    password: string;

    @Column({ type: 'bigint', nullable: true })
    nik: number;

    @Column({ nullable: true })
    phone: string;

    @Column({ type: 'enum', enum: UserRole, default: UserRole.USER })
    role: UserRole;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;

    @OneToMany(() => NewsEntity, news => news.user)
    news: NewsEntity[];

    @OneToMany(() => ProductEntity, product => product.user)
    product: ProductEntity[]

    @OneToMany(() => TransactionEntity, transaction => transaction.user)
    transaction: TransactionEntity[]
}