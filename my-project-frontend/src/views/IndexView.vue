<template>
  <div>
    <!-- 点击按钮触发退出登录事件 -->
    <el-button @click="userLogout">退出登录</el-button>
  </div>
</template>

<script setup>
import {logout} from '@/net' // 导入退出登录的 API
import router from "@/router"; // 导入路由实例

/**
 * 退出登录函数
 * 调用退出登录 API，成功后跳转到登录页面
 */
function userLogout() {
  logout(() => router.push("/")) // 调用退出登录接口，并在回调中跳转到根路径
}
</script>

<style scoped>

</style>
