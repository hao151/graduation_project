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

export default{
  components: {
   NavBar
  },

  setup(){
   

  }
   
  }


</script>


<style>
body{
  background-image: url("@/assets/img/background.png");

}

</style>


