import { GameObject } from "./GameObject";
import { body } from "./body";

export class snake extends GameObject{
    constructor(info, gamemap){
        super();
        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.body = [new body(info.r, info.c)]; //body[0]存头, 整个数组存身体
        this.speed = 5;
        this.direction = -1;  //0123上右下左
        this.status = "still";  //静止：still,  移动：move  游戏结束：die
        this.dr = [-1, 0, 1, 0];  //行偏移
        this.dc = [0, 1, 0, -1]; //列偏移

        this.steps = 0; //当前回合数
        this.next_body = null; //下一步目标位
        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;  //初始眼睛方向

        this.eye_dx = [  //移动时偏移量
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1]
        ];

        this.eye_dy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1]
        ];
    }

    start(){

    }
    update_move(){
        const dx = this.next_body.x - this.body[0].x;
        const dy = this.next_body.y - this.body[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < 1e-2){  //走到目的地
            this.body[0] = this.next_body;
            this.next_body = null;
            this.status = 'still';
            if (!this.check_tail()) this.body.pop();
        }
        else{
            const move_distance = this.speed * this.timedelta / 1000;   //一帧移动的距离
            this.body[0].x += move_distance * dx / distance;
            this.body[0].y += move_distance * dy / distance;

            if (!this.check_tail()){
                const k = this.body.length; 
                const tail = this.body[k - 1], tail_target = this.body[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update(){
        if (this.status === 'move') 
            this.update_move();
        
        this.render();
    }

    render()
    {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle = this.color;

        if (this.status === 'die'){
            ctx.fillStyle = 'white';
        }

        for (const body of this.body){
            ctx.beginPath();
            ctx.arc(body.x * L, body.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        for (let i = 1; i < this.body.length; i ++ ){
            const a = this.body[i - 1], b = this.body[i];
            if (Math.abs(a.x - b.x) < 1e-2 && Math.abs(a.y - b.y) < 1e-2) continue;
            if (Math.abs(a.x - b.x) < 1e-2) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            }
            else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8)
            }
        }

        ctx.fillStyle = 'black';
        for (let i = 0; i < 2; i ++ )
        {
            const eye_x = (this.body[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.body[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    next_step(){
        const d = this.direction;
        this.next_body = new body(this.body[0].r + this.dr[d], this.body[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1;  //重置方向
        this.status = 'move'; //更新状态
        this.steps ++ ;

        const k = this.body.length;
        for (let i = k; i > 0; i -- ){
            this.body[i] = JSON.parse(JSON.stringify(this.body[i - 1]));
        }

        if (this.gamemap.check_over(this.next_body)){
            this.status = 'die';
        }
    }

    set_direction(d){
        this.direction = d;

    }

    check_tail(){
        if (this.steps < 6) return true;
        if (this.steps % 3 === 0) return true;
        return false;
        
    }


}