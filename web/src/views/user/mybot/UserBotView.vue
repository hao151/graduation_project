<template>
    <div class="container">
        <div class="row">

            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src=$store.state.user.photo alt="">
                    </div>
                </div>
            </div>

            <div class="col-9"> 
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span style="font-size: 110%;">我的bot</span>
                        <button type="button" class="btn btn-success float-end btn-sm" data-bs-toggle="modal" data-bs-target="#add-bot">
                        创建bot
                        </button>


                        <div class="modal fade" id="add-bot" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-xl">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">创建bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">

                                <div class="mb-3">
                                <label for="botname" class="form-label">昵称</label>
                                <input v-model="new_bot.name" type="text" class="form-control" id="botname" placeholder="给你的bot取个名字吧">
                                </div>
                                <div class="mb-3">
                                <label for="description" class="form-label">简介</label>
                                <textarea v-model="new_bot.description" class="form-control" id="description" rows="3" placeholder="介绍一下你的bot吧"></textarea>
                                </div>
                                <div class="mb-3">
                                <label for="code" class="form-label">源代码</label>

                                <VAceEditor v-model:value="new_bot.code" lang="c_cpp"
                                                theme="textmate" style="height: 300px" :options="{
                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                    enableSnippets: true, // 启用代码段
                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                    fontSize: 18, //设置字号
                                                    tabSize: 4, // 标签大小
                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                    highlightActiveLine: true,
                                                }" />

                            </div>

                            </div>
                            <div class="modal-footer">
                                <div class="error_message" style="color:red;">{{ new_bot.error_message }}</div>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                            </div>
                            </div>
                        </div>
                        </div>
                    </div>

                    <div class="card-body">

                        <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Bot名称</th>
                                <th>创建时间</th>    
                                <th>操作</th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr v-for="bot in bots" :key="bot.id">
                                <td>{{ bot.botName }}</td>
                                <td>{{ bot.createTime }}</td>
                                <td style="white-space: nowrap;"> 
                                    <button type="button" class="btn btn-primary btn-sm" style="margin-right: 10px;"
                                    data-bs-toggle="modal" :data-bs-target="'#update-bot-modal' + bot.id">修改</button>
                                    <button type="button" class="btn btn-danger btn-sm" @click="remove_bot(bot)">删除</button>


                                    <div class="modal fade" :id="'update-bot-modal' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-xl">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5">创建bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">

                                <div class="mb-3">
                                <label for="botname" class="form-label">昵称</label>
                                <input v-model="bot.botName" type="text" class="form-control" id="botname" placeholder="给你的bot取个名字吧">
                                </div>
                                <div class="mb-3">
                                <label for="description" class="form-label">简介</label>
                                <textarea v-model="bot.description" class="form-control" id="description" rows="3" placeholder="介绍一下你的bot吧"></textarea>
                                </div>
                                <div class="mb-3">
                                <label for="code" class="form-label">源代码</label>
                                <VAceEditor v-model:value="bot.code"
                                                                lang="c_cpp" theme="textmate" style="height: 300px"
                                                                :options="{
                                                                    enableBasicAutocompletion: true, //启用基本自动完成
                                                                    enableSnippets: true, // 启用代码段
                                                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                                                    fontSize: 18, //设置字号
                                                                    tabSize: 4, // 标签大小
                                                                    showPrintMargin: false, //去除编辑器里的竖线
                                                                    highlightActiveLine: true,
                                                                }" />
                                </div>

                            </div>
                            <div class="modal-footer">
                                <div class="error_message" style="color:red;">{{bot.error_message }}</div>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存</button>
                            </div>
                            </div>
                        </div>
                        </div>
                                </td>
                            </tr>
                        </tbody>
                        </table>

                    </div>
                </div>
            </div>
        </div>
    </div>
   
</template>
    
    
<script>
import { ref, reactive } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery'
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';



export default{
    components: {
        VAceEditor
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" +
            require("ace-builds").version +
            "/src-noconflict/")

        const store = useStore();
        let bots = ref([]);

        const new_bot = reactive({
            name:"",
            description:"",
            code:"",
            error_message:"",
        })

        const get_bots = ()=>{
            $.ajax({
                url:'http://127.0.0.1:3000/user/bot/getlist/',
                type:"get",
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    bots.value = resp;
                    console.log(resp);
                }
            })
        }
        get_bots();

        const add_bot = ()=>{
            new_bot.error_message = "";
            $.ajax({
                url:'http://127.0.0.1:3000/user/bot/add/',
                type:'post',
                data:{
                    name:new_bot.name,
                    description:new_bot.description,
                    code:new_bot.code,
                },
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === 'success'){
                        new_bot.name = "";
                        new_bot.code = "",
                        new_bot.description = "",
                        Modal.getInstance("#add-bot").hide();
                        get_bots();
                    }else{
                        new_bot.error_message = resp.error_message;
                    }
                }

            })
        }

        const remove_bot = (bot)=>{
            $.ajax({
                url:'http://127.0.0.1:3000/user/bot/remove/',
                type:'post',
                data:{
                    bot_id:bot.id
                },
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === 'success'){
                        get_bots();
                    }
                }
            })

        }

        const update_bot = (bot)=>{
            bot.error_message = "";
            $.ajax({
                url:'http://127.0.0.1:3000/user/bot/update/',
                type:'post',
                data:{
                    bot_id:bot.id,
                    name:bot.botName,
                    description:bot.description,
                    code:bot.code,
                },
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    if (resp.error_message === 'success'){
                        Modal.getInstance('#update-bot-modal' + bot.id).hide();
                        get_bots();
                    }else{
                        bot.error_message = resp.error_message;
                    }
                }

            })
        }


        return {
            bots,
            new_bot,
            add_bot,
            remove_bot,
            update_bot,
        }
    }
}

</script>
    
<style scoped>
button{
            display: inline-block;
            max-width: 80px; 
        }

</style>