<template>
  <div class="container">
    <el-card class="card account-info">
      <template #header>
        <el-icon :size="23">
          <User/>
        </el-icon>
        <el-button text type="primary" @click="accountInfo.editUserInfo">编辑资料</el-button>
      </template>
      <template v-slot>
        <el-popover placement="bottom" :width="300" trigger="hover">
          <template #reference>
            <el-avatar :size="80" :src="avatar"></el-avatar>
          </template>
          <template v-slot>
            <el-descriptions direction="horizontal" column="1" border title="个人名片">
              <el-descriptions-item label="用户名" label-align="center">{{ nickname }}</el-descriptions-item>
              <el-descriptions-item label="真实姓名" label-align="center">{{ username }}</el-descriptions-item>
              <el-descriptions-item label="电子邮箱" label-align="center">{{ email }}</el-descriptions-item>
              <el-descriptions-item label="联系方式" label-align="center">{{ phoneNumber }}</el-descriptions-item>
              <el-descriptions-item label="当前ip" label-align="center">{{ ip }}</el-descriptions-item>
              <el-descriptions-item label="所属部门" label-align="center">{{ department }}</el-descriptions-item>
              <el-descriptions-item label="所属岗位" label-align="center">{{ duty }}</el-descriptions-item>
            </el-descriptions>
          </template>
        </el-popover>
        <span class="name-display">{{ nickname }}</span>
        <el-tag size="large" class="tag">
          <el-icon>
            <HomeFilled/>
          </el-icon>
          {{ organization }}
        </el-tag>
      </template>
    </el-card>
    <el-card class="card with-table">
      <template #header>
        <span>用户自定义组件</span>
        <el-button plain text type="primary">更多</el-button>
      </template>
      <template v-slot>
        <el-table
            :data="customModules"
            row-key="name"
            ref="table"
            fit
        >
          <el-table-column label="名称">
            <template v-slot="scope">
              <el-button type="primary" text link>{{ scope.row.name }}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="类型" prop="type"></el-table-column>
          <el-table-column label="创建者" prop="creator"></el-table-column>
          <el-table-column label="创建时间" prop="date"></el-table-column>
          <el-table-column label="备注" prop="remark"></el-table-column>
        </el-table>
      </template>
    </el-card>
    <div class="small-cards">
      <el-card class="card small-card">
        <template #header>
          <el-icon :size="23">
            <House></House>
          </el-icon>
          <span>团队</span>
        </template>
        <template v-slot>
          <span>主持或加入的团队</span>
          <strong>{{ teamNum }}</strong>
        </template>
      </el-card>
      <el-card class="card small-card">
        <template #header>
          <el-icon :size="23">
            <Document></Document>
          </el-icon>
          <span>资源</span>
        </template>
        <template v-slot>
          <span>创建或引用的资源</span>
          <strong>{{ resourceNum }}</strong>
        </template>
      </el-card>
    </div>
    <el-card class="card with-table">
      <template #header>
        <span class="table-card-header-span">相关用户</span>
        <el-button plain text type="primary">更多</el-button>
      </template>
      <template v-slot>
        <el-table
            :data="relatedUserData"
            row-key="name"
            ref="table"
            fit
        >
          <el-table-column label="用户名">
            <template v-slot="scope">
              <el-button type="primary" text link>{{ scope.row.username }}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="来自" prop="source"></el-table-column>
          <el-table-column label="团队职务" prop="title"></el-table-column>
          <el-table-column label="备注" prop="remark"></el-table-column>
        </el-table>
      </template>
    </el-card>
  </div>
  <el-dialog v-model="dialog.dialogVisible.value" title="编辑资料" width="40%"
             @open="dialog.editBasicInfoPane.initForm">
    <template v-slot>
      <el-tabs v-model="dialog.activePane.value">
        <el-tab-pane name="editBasicInfoPane" label="基本信息编辑">
          <el-form :ref="dialog.editBasicInfoPane.editBasicInfoFormEl" :model="dialog.editBasicInfoPane.formData"
                   label-width="100px" label-suffix="："
                   :rules="dialog.editBasicInfoPane.formRules">
            <el-form-item prop="nickname" label="用户名">
              <el-input v-model="dialog.editBasicInfoPane.formData.nickname"/>
            </el-form-item>
            <el-form-item prop="username" label="真实姓名">
              <el-input v-model="dialog.editBasicInfoPane.formData.username"/>
            </el-form-item>
            <el-form-item prop="email" label="电子邮箱">
              <el-input v-model="dialog.editBasicInfoPane.formData.email"/>
            </el-form-item>
            <el-form-item prop="phoneNumber" label="联系方式">
              <el-input v-model="dialog.editBasicInfoPane.formData.phoneNumber"/>
            </el-form-item>
            <el-form-item prop="department" label="所属部门">
              <el-input v-model="dialog.editBasicInfoPane.formData.department"/>
            </el-form-item>
            <el-form-item prop="post" label="所属岗位">
              <el-input v-model="dialog.editBasicInfoPane.formData.post"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane name="editPasswordPane" label="用户密码修改">
          <el-form :ref="dialog.editPasswordPane.editPasswordFormEl" :model="dialog.editPasswordPane.formData"
                   label-width="100px" label-suffix="："
                   :rules="dialog.editPasswordPane.formRules">
            <el-form-item prop="originPassword" label="原密码">
              <el-input v-model="dialog.editPasswordPane.formData.originPassword"/>
            </el-form-item>
            <el-form-item prop="newPassword" label="新密码">
              <el-input v-model="dialog.editPasswordPane.formData.newPassword" show-password/>
            </el-form-item>
            <el-form-item prop="rePassword" label="重复密码">
              <el-input v-model="dialog.editPasswordPane.formData.rePassword" show-password/>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </template>
    <template #footer>
      <el-button circle type="info" icon="Refresh" @click="dialog.handleReset"/>
      <el-button circle type="primary" icon="Check" @click="dialog.handleSubmit"/>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {Document, HomeFilled, House, User} from "@element-plus/icons-vue";
import {useUserStore} from "@/store/user";
import {storeToRefs} from "pinia";
import {ElMessage, FormInstance, FormRules} from "element-plus"

const userStore = useUserStore()

const {
  nickname,
  username,
  ip,
  email,
  teamNum,
  phoneNumber,
  resourceNum,
  avatar,
  department,
  post,
  organization
} = storeToRefs(userStore)

const accountInfo = {
  editUserInfo: () => {
    dialog.dialogVisible.value = true;
  }
}

const dialog = {
  dialogVisible: ref<boolean>(false),
  activePane: ref("editBasicInfoPane"),
  editBasicInfoPane: {
    formData: reactive({
      username: "",
      nickname: "",
      email: "",
      phoneNumber: "",
      department: "",
      post: ""
    }),
    formRules: <FormRules>{
      username: {
        required: true,
        message: "请输入真实姓名",
        trigger: "blur"
      },
      nickname: {
        required: true,
        message: "请输入用户名",
        trigger: "blur"
      },
      email: {
        required: true,
        type: "email",
        message: "请输入正确的邮箱格式",
        trigger: "blur"
      },
      phoneNumber: {
        required: true,
        validator: (rule, value, callback) => {
          if (!/^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/.test(value || !/[0-9]{3,4}-[0-9]{8}/)) {
            callback(new Error("手机或固定电话号码不正确"))
          }
        },
        trigger: "blur"
      }
    },
    editBasicInfoFormEl: ref<FormInstance>(),
    initForm: () => {
      dialog.editBasicInfoPane.formData.nickname = nickname.value
      dialog.editBasicInfoPane.formData.username = username.value
      dialog.editBasicInfoPane.formData.email = email.value
      dialog.editBasicInfoPane.formData.phoneNumber = phoneNumber.value
      dialog.editBasicInfoPane.formData.department = department.value
      dialog.editBasicInfoPane.formData.post = post.value
    }
  },
  editPasswordPane: {
    formData: reactive({
      originPassword: "",
      newPassword: "",
      rePassword: ""
    }),
    formRules: <FormRules>{
      originPassword: {
        required: true,
        validator: (rule, value, callback) => {
          if (value.trim() === ("")) {
            callback(new Error("请输入原密码"))
          }
        }, trigger: "blur"
      },
      newPassword: {
        required: true,
        validator: (rule, value, callback) => {
          if (!/(?!^(\d+|[a-zA-Z]+|[~!@#$%^&*()_.]+)$)^[\w~!@#$%^&*()_.]{8,16}$/.test(value)) {
            callback(new Error("密码格式不正确，应为十六位字母、数字、特殊符号的组合"))
          } else if (value.equals(dialog.editPasswordPane.formData.originPassword)) {
            callback(new Error("新密码不应与原密码相同"))
          }
        }, trigger: "blur"
      },
      rePassword: {
        required: true,
        validator: (rule, value, callback) => {
          if (!(value === dialog.editPasswordPane.formData.newPassword)) {
            callback(new Error("两次密码输入不一致"))
          } else if (value.trim() === "") {
            callback(new Error("请重复输入密码"))
          }
        }
      }
    },
    editPasswordFormEl: ref<FormInstance>()
  },
  handleReset: () => {
    let formEl = dialog.activePane.value === "editBasicInfoPane" ? dialog.editBasicInfoPane.editBasicInfoFormEl.value : dialog.editPasswordPane.editPasswordFormEl.value as FormInstance
    formEl?.resetFields()
  },
  handleSubmit: async () => {
    console.log(dialog.activePane.value)
    let formEl = dialog.activePane.value === "editBasicInfoPane" ? dialog.editBasicInfoPane.editBasicInfoFormEl.value : dialog.editPasswordPane.editPasswordFormEl.value as FormInstance
    formEl?.validate()
        .then(() => {
          //TODO 发请求修改信息
        })
        .catch(() => {
          ElMessage({
            message: "错误的表单信息",
            type: "error",
            duration: 2000,
            showClose: true,
          })
        })
  }
}

const customModules = reactive([
  {
    id: '100',
    name: '模块1',
    type: '模块',
    creator: '张三',
    date: '2020-2-11 00:00:00',
    remark: 'None'
  }
])

const relatedUserData = reactive([
  {
    id: '1',
    username: '张三',
    source: '团队1',
    title: '项目管理',
    remark: '无'
  }
])

onMounted(() => {
  userStore.getUserInfo()
  console.log(dialog.dialogVisible)
})
</script>

<style scoped lang="scss">
.container {
  height: 100%;
  width: 100%;
  display: grid;
  grid-template-columns: 2fr 5fr;
  grid-template-rows: 1fr 1fr;
  column-gap: 5px;
  row-gap: 5px;

  .card {
    :deep .el-card__header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 50px;
      font-size: 15pt;
    }

    :deep .el-card__body {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }

  .small-cards {
    display: grid;
    grid-template-rows: 1fr 1fr;
    row-gap: 5px;

    :deep .el-card__header {
      justify-content: start;
      letter-spacing: 6px;
      font-weight: normal;

      .el-icon {
        margin-right: 20px;
      }
    }

    :deep .el-card__body {

      span {
        margin: 10px;
        font-size: 13pt;
      }

      strong {
        margin: 10px;
        font-size: 30pt;
      }
    }
  }

  .account-info {
    display: flex;
    flex-direction: column;
    align-items: center;

    :deep .el-card__body {
      display: flex;
      height: 300px;
      flex-direction: column;
      align-items: center;
      justify-content: space-evenly;
    }

    .name-display {
      font-size: 16pt;
      margin: 10px 0;
    }

    :deep .el-tag__content {
      display: flex;
      align-items: center;
    }
  }

  .with-table {
    :deep(.el-card__body) {
      padding: 5px;
    }
  }
}
</style>