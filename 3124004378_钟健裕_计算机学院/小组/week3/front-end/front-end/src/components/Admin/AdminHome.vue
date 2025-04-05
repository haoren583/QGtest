<template>
    <el-container class="layout-container-demo" style="height: 100vh">
        <el-aside width="200px" style="background-color: white;">
            <el-scrollbar>
                <div style="padding: 15px">
                    <h2>管理员菜单</h2>
                </div>
                <el-menu :default-openeds="['1', '3']">
                    <el-sub-menu index="1">
                        <template #title>
                            <el-icon>
                                <UserFilled />
                            </el-icon>用户管理

                        </template>
                        <el-menu-item-group>
                            <router-link :to="{ name: 'Attest' }" style="text-decoration: none;">
                                <el-menu-item index="1-1">认证申请管理</el-menu-item>
                            </router-link>
                            <router-link :to="{ name: 'UserManage' }" style="text-decoration: none;">
                                <el-menu-item index="1-2">用户信息管理</el-menu-item>
                            </router-link>
                            <router-link :to="{ name: 'CommentManage' }" style="text-decoration: none;">
                                <el-menu-item index="1-3">用户评论审核</el-menu-item>
                            </router-link>

                        </el-menu-item-group>
                    </el-sub-menu>

                    <el-sub-menu index="2">
                        <template #title>
                            <el-icon><icon-menu /></el-icon>系统管理
                        </template>
                        <el-menu-item-group>
                            <router-link :to="{ name: 'DoctorManage' }" style="text-decoration: none;">
                                <el-menu-item index="2-1">医生信息管理</el-menu-item> </router-link>
                            <router-link :to="{ name: 'DepartmentManage' }" style="text-decoration: none;">
                                <el-menu-item index="2-2">科室信息管理</el-menu-item>
                            </router-link>
                            <router-link :to="{ name: 'PatientManage' }" style="text-decoration: none;">
                                <el-menu-item index="2-3">患者信息管理</el-menu-item>
                            </router-link>

                        </el-menu-item-group>
                    </el-sub-menu>
                </el-menu>
            </el-scrollbar>
        </el-aside>

        <el-container>
            <el-header style="text-align: right; font-size: 12px">
                <div class="toolbar">
                    <el-dropdown>
                        <el-icon style="margin-right: 8px; margin-top: 1px">
                            <setting />
                        </el-icon>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>View</el-dropdown-item>
                                <el-dropdown-item>Add</el-dropdown-item>
                                <el-dropdown-item>Delete</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                    <span>{{ store.token.userName }}</span>
                </div>
            </el-header>

            <el-main>
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Menu as IconMenu,  Setting } from '@element-plus/icons-vue'
import { TokenStore } from '../../Mytoken'

const store = TokenStore();



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
