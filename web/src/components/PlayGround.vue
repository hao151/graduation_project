<template>
    <div class="playground">
    <GameMap> </GameMap>
    </div>
</template>


<script>
import GameMap from './GameMap.vue';
export default{
    components:{
    GameMap
    }
}

</script>


<style scoped>

div.playground{
    width:80vw;
    height:70vh;
    
    margin:40px auto;
}
</style>