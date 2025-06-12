/* eslint-disable prettier/prettier */
import { UserEntity } from "src/user/user.entity";
import { Entity, PrimaryGeneratedColumn, Column, CreateDateColumn, UpdateDateColumn, ManyToOne, JoinColumn } from "typeorm";

@Entity('news')
export class NewsEntity {
    @PrimaryGeneratedColumn()
    id: number;
    
    @Column()
    user_id: number;

    @Column()
    name: string;

    @Column()
    desc: string;

    @Column()
    image: string;

    @Column()
    link: string;

    @CreateDateColumn()
    created_at: Date;

    @UpdateDateColumn()
    updated_at: Date;

    @ManyToOne(() => UserEntity, user => user.news, { onDelete: 'CASCADE' })
    @JoinColumn({ name: 'user_id' })
    user: UserEntity;
}