const GAME_OBJECTS = [];

export class GameObject{
    constructor(){
        GAME_OBJECTS.push(this);
        this.started = false;
        this.timedelta = 0;
    }

    start(){

    }

    update(){

    }

    before_destory(){

    }

    destory(){
        this.before_destory();
        for(let i in GAME_OBJECTS){
            const obj = GAME_OBJECTS[i];
            if (obj === this){
                GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}
let last_time;

const step = now_time=>{
    for (let obj of GAME_OBJECTS){
        if(!obj.started){
            obj.started = true;
            obj.start();
        }
        else{
            obj.timedelta = now_time - last_time;
            obj.update();
        }
    }
    last_time = now_time;
    requestAnimationFrame(step);
}

requestAnimationFrame(step)