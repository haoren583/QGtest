<template>
    <div class="main">
        <div class="role-box">
            <div class="head">
                <div class="title"><b>校医院预约挂号管理系统</b></div>
            </div>
            <div class="body">

                <div style="margin: 50px;">
                    <router-link :to="{ name: 'User' }">
                        <el-button plain class="button-style">一般用户</el-button>
                    </router-link>
                </div>

                <div style="margin: 50px;">
                    <router-link :to="{ name: 'Doctor' }">
                        <el-button plain class="button-style">医生用户</el-button>
                    </router-link>
                </div>

                <div style="margin: 50px;">
                    <router-link :to="{ name: 'Admin' }">
                        <el-button plain class="button-style">管理员</el-button>
                    </router-link>
                </div>

            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
</script>

<style lang="scss" scoped>
.main {
    width: 100vw;
    height: 100vh;
    /* background: url('../assets/hz.svg'); */

    .role-box {
        width: 1200px;
        height: 800px;
        box-shadow: 0 0 5px #000000;
        position: absolute;
        top: calc(50vh - 400px);
        left: calc(50vw - 600px);
        border-radius: 20px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

    .head {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 150px;

        .logo {
            width: 60px;
            height: 60px;
            border-radius: 50px;
        }

        .title {
            font-size: 30px;
            font-weight: bold;
            color: var(--el-color-primary);
            margin-left: 20px;
        }
    }

    .button-style {
        width: 400px;
        height: 80px;
        font-size: 24px;
        cursor: pointer;
    }
}
</style>