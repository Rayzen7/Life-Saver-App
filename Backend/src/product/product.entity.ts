/* eslint-disable prettier/prettier */
import { CategoryEntity } from "src/category/category.entity";
import { TransactionEntity } from "src/transaction/transaction.entity";
import { UserEntity } from "src/user/user.entity";
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, JoinColumn, OneToMany } from "typeorm";

@Entity('product')
export class ProductEntity {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    user_id: number;

    @Column()
    category_id: number;

    @Column()
    name: string;

    @Column()
    image: string;

    @Column({ type: 'text' })
    desc: string;

    @Column({ type: 'text' })
    dosis: string;

    @Column({ type: 'bigint' })
    quantity: number;

    @Column({ type: 'bigint' })
    price: number;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;

    @ManyToOne(() => UserEntity, user => user.product, { onDelete: 'CASCADE' })
    @JoinColumn({ name: 'user_id' })
    user: UserEntity

    @ManyToOne(() => CategoryEntity, category => category.product, { onDelete: 'CASCADE' })
    @JoinColumn({ name: 'category_id' })
    category: CategoryEntity

    @OneToMany(() => TransactionEntity, transaction => transaction.product)
    transaction: TransactionEntity[]
}