<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-4">
                <div class="user-select-bot">
                    <div style="text-align: center; font-size: 24px; color: black; font-weight: 600;">请选择出战英雄</div>
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                    <option value="-1" selected>亲自出战</option>
                    <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.botName }}</option>

                    </select>
                </div>
            </div>

            <div class="col-4">
                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>

            <div class="col-12" style="text-align: center; margin-top: 12vh;">
                <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg" style="width:80px;">{{ match_btn_info }}</button>
            </div>
        </div>


    </div>
</template>


<script>
import {ref} from 'vue'
import {useStore} from 'vuex'
import $ from 'jquery'

export default{
    setup(){

        const store = useStore();
        let match_btn_info = ref('匹配');
        let bots = ref([]);
        let select_bot = ref("-1");

        const click_match_btn =()=>{
            if (match_btn_info.value === '匹配'){
                match_btn_info.value = '取消';
                store.state.pk.socket.send(JSON.stringify({
                    event:"start-matching",
                    bot_id:select_bot.value,
                }));   
            }
            else if(match_btn_info.value === '取消'){
                match_btn_info.value = '匹配';
                store.state.pk.socket.send(JSON.stringify({
                    event:"stop-matching",
                }));   
            }
        }

        const get_bots = ()=>{
            $.ajax({
                url:'http://127.0.0.1:3000/user/bot/getlist/',
                type:"get",
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    bots.value = resp;
                }
            })
        }

        get_bots();

        return{
        match_btn_info,
        click_match_btn,
        bots,
        select_bot,
    }
    }

    
}

</script>


<style scoped>

div.matchground{
    width:60vw;
    height:70vh;
    margin:40px auto;
    background-color: rgba(50, 50, 50, 0.4);
}

div.user-photo{
    text-align: center;
}
div.user-photo > img{
    border-radius: 50%;
    width:20vh;
    margin-top: 10vh;

}

div.user-username{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    margin-top: 3vh;
    color:white;
}

div.user-select-bot{
    margin-top: 20vh;
}
div.div.user-select-bot > select{
    width:60%;
    margin:0 auto;
}

</style>