<template>
    <div>
        <CommonContent>
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>玩家ID</th>
                        <th>天梯积分</th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="user in users" :key="user.id">
                        <td>
                            <img :src="user.photo" alt="" class="record-user-photo">
                            &nbsp;
                            <span class="record-user-username">{{ user.username }}</span>
                        </td>

                        <td>
                           {{user.rating}}
                        </td>
                       
                    </tr>

                </tbody>
            </table>

            <nav aria-label="Page navigation example">
            <ul class="pagination" style="float: right">
                <li class="page-item" @click="click_page(-1)">
                <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
                </li>
                <li :class="'page-item '+ page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                    <a class="page-link" href="#">{{ page.number }}</a>
                </li>
                
                <li class="page-item" @click="click_page(0)">
                <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
                </li>
            </ul>
            </nav>
        </CommonContent>
    </div>
</template>
    
    
<script>
    import CommonContent from "@/components/CommonContent.vue";
    import {useStore} from 'vuex';
    import $ from 'jquery';
    import {ref} from 'vue';

export default{
    components:{
        CommonContent
    },
    setup(){
        const store = useStore();
        let users = ref([]);
        let current_page = 1;
        let total_users = 0;
        let pages = ref([]);

        const click_page = page=>{
            if (page === -1) page = current_page - 1;
            else if(page === 0) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_users / 3));
            if (page >= 1 && page <= max_pages){
                get_page(page);
            }
        }

        const update_pages = ()=>{
            let max_pages = parseInt(Math.ceil(total_users / 3));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ){
                if (i >= 1 && i <= max_pages){
                    new_pages.push({
                        number:i,
                        is_active:i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const get_page = page =>{
            current_page = page;
            $.ajax({
                url:'http://127.0.0.1:3000/ranklist/getlist/',
                type:'get',
                data:{
                    page,
                },
                headers:{
                    Authorization:"Bearer " + store.state.user.token,
                },
                success(resp){
                    users.value = resp.users;
                    total_users = resp.users_count;
                    update_pages();
                },
                error(resp){
                    console.log(resp);
                }
            })
        }
        get_page(current_page);

        return {
            users,
            pages,
            click_page,
            total_users,
        }
    }
}
</script>
    
<style scoped>
img.record-user-photo{
    width:4vh;
    border-radius: 50%;
}
    
</style>