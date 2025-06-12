/* eslint-disable prettier/prettier */
import { HttpException, Injectable } from "@nestjs/common";
import { UserService } from "src/user/user.service";
import * as bcrypt from 'bcryptjs';
import { JwtService } from "@nestjs/jwt";
import { AuthDto } from "./auth.dto";
import { instanceToPlain } from "class-transformer";

@Injectable()
export class AuthService {
    constructor(
        private userService: UserService,
        private jwtService: JwtService,
    ) {}

    async login(data: AuthDto): Promise<any> {
        if (!data.email || !data.password) {
            throw new HttpException({
                statusCode: 422,
                message: "Email or Password Must Be Required",
            }, 422);
        }

        const user = await this.userService.findByEmail(data?.email);
        if (!user || !(await bcrypt.compare(data.password, user?.password))) {
            throw new HttpException({
                statusCode: 422,
                message: "Invalid Credentials",
            }, 422);
        }

        const userPayload = {
            id: user.id,
            email: user.email,
            role: user.role
        }

        const token = this.jwtService.sign(userPayload);
        return {
            access_token: token,
            message: "Login Success",
            user: user
        }
    }

    async me(id: number) {
        const user = await this.userService.findOne(id);
        return instanceToPlain(user);
    }
}