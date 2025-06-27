/* eslint-disable prettier/prettier */
import { ProductEntity } from "src/product/product.entity";
import { UserEntity } from "src/user/user.entity";
import { Entity, Column, PrimaryGeneratedColumn, CreateDateColumn, UpdateDateColumn, JoinColumn, ManyToOne } from "typeorm";

export enum TransactionStatus {
    PENDING = 'pending',
    SUCCESS = 'success',
    CANCELED = 'canceled'
}

@Entity('transaction')
export class TransactionEntity {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    user_id: number;

    @Column()
    product_id: number;

    @Column({ type: 'enum', enum: TransactionStatus, default: TransactionStatus.PENDING })
    status: TransactionStatus;

    @Column()
    address: string;

    @Column({ type: 'bigint' })
    quantity: number;

    @Column({ type: 'bigint' })
    total: number;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;

    @ManyToOne(() => UserEntity, user => user.transaction, { onDelete: 'CASCADE' })
    @JoinColumn({ name: 'user_id' })
    user: UserEntity;

    @ManyToOne(() => ProductEntity, product => product.transaction, { onDelete: 'CASCADE' })
    @JoinColumn({ name: 'product_id' })
    product: ProductEntity;
}   