/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
import { Body, Post, Controller, UseGuards, HttpCode } from "@nestjs/common";
import { AuthService } from "./auth.service";
import { AuthDto } from "./auth.dto";
import { JwtAuthGuard } from "./jwt.guard";

@Controller('/auth')
export class AuthController {
    constructor(
        private authService: AuthService
    ) {}

    @Post('/login')
    @HttpCode(200)
    async login(@Body() data: AuthDto): Promise<any> {
        const user = await this.authService.login(data);
        return user;
    }

    @Post('/logout')
    @HttpCode(200)
    @UseGuards(JwtAuthGuard)
    logout() {
        return {
            message: "Logout Success"
        }   
    }
}