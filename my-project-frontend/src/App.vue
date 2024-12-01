<script setup>
// 引入 useDark 和 useToggle 函数
import {useDark, useToggle} from '@vueuse/core'

// 配置深色模式
useDark({
  selector: 'html',
  attribute: 'class',
  valueDark: 'dark',
  valueLight: 'light'
})

// 监听深色模式变化，并切换 toggle 状态
useDark({
  onChanged(dark) {
    useToggle(dark)
  }
})


</script>

<template>
  <!-- 定义一个 header 标签，作为页面的头部 -->
  <header>
    <!-- 使用一个 div 元素作为包裹层，通常用于布局和样式控制 -->
    <div class="wrapper">
      <!-- 使用 router-view 组件来渲染路由匹配到的组件 -->
      <router-view/>
    </div>
  </header>
</template>

<style scoped>
/* 选择 header 元素，设置其行高为 1.5 倍字体大小 */
header {
  line-height: 1.5;
}
</style>

