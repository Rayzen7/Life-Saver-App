/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/no-unsafe-argument */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
import { Body, Post, Controller, UseGuards, HttpCode, Req, Get } from "@nestjs/common";
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

    @Get('/me')
    @UseGuards(JwtAuthGuard)
    async me(@Req() req: any) {
        const id = await req.user.id;
        const user = await this.authService.me(id);
        return {
            user: user
        }
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