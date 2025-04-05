<template>
    <el-container class="layout-container-demo" style="height: 100vh">

        <el-header style="text-align: right; font-size: 12px">
            <h3
                style="float: left; display: flex; align-items: center; justify-content: center; height: 100%; margin: 0; padding: 0;">
                用户菜单</h3>
            <el-button :icon="DArrowLeft" circle size="large"
                style="float: left; margin-top:12px; margin-left: 110px; padding: 0; ">
            </el-button>
            <div class="toolbar">
                <el-dropdown>
                    <el-icon style="margin-right: 8px; margin-top: 1px">
                        <setting />
                    </el-icon>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                <span>{{ headRight }}</span>
            </div>
        </el-header>

        <el-container>


            <el-aside width="200px" style="background-color: white;">
                <el-scrollbar>
                    <el-menu>
                        <router-link :to="{ name: 'Appointment' }" style="text-decoration: none;">
                            <el-menu-item index="1">
                                <span>预约挂号</span>
                            </el-menu-item>
                        </router-link>
                        <router-link :to="{ name: 'History' }" style="text-decoration: none;">
                            <el-menu-item index="2">
                                <span>历史挂号</span>
                            </el-menu-item>
                        </router-link>
                        <router-link :to="{ name: 'PIM' }" style="text-decoration: none;">
                            <el-menu-item index="3">
                                <span>个人信息管理</span>
                            </el-menu-item>
                        </router-link>
                        <router-link :to="{ name: 'PhoneChange' }" style="text-decoration: none;">
                            <el-menu-item index="4">
                                <span>修改手机号</span>
                            </el-menu-item>
                        </router-link>
                        <router-link :to="{ name: 'PasswordChange' }" style="text-decoration: none;">
                            <el-menu-item index="5">
                                <span>修改密码</span>
                            </el-menu-item>
                        </router-link>
                    </el-menu>
                </el-scrollbar>
            </el-aside>
            <el-divider direction="vertical" style="margin: 10px 0;height: 95%;"></el-divider>
            <el-main>
                <router-view style="height: 100%;" />
            </el-main>
        </el-container>
    </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Menu as IconMenu, Setting } from '@element-plus/icons-vue'
import { TokenStore } from '../../Mytoken'
import { DArrowLeft } from '@element-plus/icons-vue'
 
const store = TokenStore();
const headRight = (TokenStore().token?.userName)||'未登录';



const item = {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
}
const tableData = ref(Array.from({ length: 20 }).fill(item))
</script>

<style scoped>
.layout-container-demo .el-header {
    position: relative;
    background-color: var(--el-color-primary-light-7);
    color: var(--el-text-color-primary);
}

.layout-container-demo .el-aside {
    color: var(--el-text-color-primary);
    background: var(--el-color-primary-light-8);
}

.layout-container-demo .el-menu {
    border-right: none;
}

.layout-container-demo .el-main {
    padding: 0;
}

.layout-container-demo .toolbar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    right: 20px;
}
</style>
