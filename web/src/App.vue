<template>
  <nav>
    <div>{{ bot_name }}</div>
    <div>{{ bot_rating }}</div>
  </nav>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';
export default{
  name:"app",
  setup:()=>{
    let bot_name = ref("");
    let bot_rating = ref("");

    $.ajax({
      url:"http://127.0.0.1:3000/pk/getbotinfo/",
      type:"GET",
      success: resp => {
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
      }
    });

    return {
      bot_name,
      bot_rating
    }

  }
}

</script>



<style>

</style>


