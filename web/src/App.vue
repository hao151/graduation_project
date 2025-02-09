<template>
  <nav>
    <NavBar></NavBar>
  </nav>
  <router-view/>
</template>

<script>
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import NavBar from './components/NavBar.vue'
//import $ from 'jquery'

export default{
  components: {
   NavBar
  },

  setup(){
    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/token/",
    //   type: "post",
    //   data:{
    //     username:'hehao',
    //     password:'123',
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   } 
    // });

    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/info/",
    //   type: "get",
    //   headers:{
    //     Authorization:"Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkZWRmZGJhZGVmZDM0YmM4OGRiY2RmZjhiYWQwNTYwNCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTczOTA3NzIyMSwiZXhwIjoxNzQwMjg2ODIxfQ.0uQ1e_PjCKNFzLpnw3ruARbMJUwD1ZiCd-1KQ3Nw7Ys"
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // });

    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/register/",
    //   type:"post",
    //   data:{
    //     username:'hehe',
    //     password:"123",
    //     confirmPassword:"123",
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // })


  }
   
  }


</script>



<style>

</style>


