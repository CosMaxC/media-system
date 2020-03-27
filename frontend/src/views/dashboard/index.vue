<template>
  <div class="dashboard-container">
    <el-container>
      <el-header>我的媒体库</el-header>
      <el-main>
        <el-row :gutter="20">
          <el-col v-for="(item, index) in medias" :key="index" :xs="12" :sm="12" :lg="6">
            <el-container>
              <el-main> <vue-hover-mask class="vue-hover-mask">
                <svg-icon icon-class="media" style="color: white;height: 30%;width: 30%;text-align:center;" />
                <template v-slot:action>
                  <!--                  <span style="font-size: 20px">{{ item.name }}</span>-->
                  <svg-icon icon-class="update" class="svg-icon-action hidden-sm-and-down" @click="openUpdateDialog(item.id)" />
                  <svg-icon icon-class="play" class="hidden-md-and-up" style="color: white;height: 50%;width: 50%;text-align:center;" @click="handleClick(item.id)" />
                  <svg-icon icon-class="play" class="svg-icon-action  hidden-sm-and-down" @click="handleClick(item.id)" />
                  <svg-icon icon-class="delete" class="svg-icon-action hidden-sm-and-down" @click="openDeleteDialog(item.id)" />
                </template>
              </vue-hover-mask></el-main>
              <el-footer style="margin-top:-40px;margin-left: -10px"><el-link @click="handleClick(item.id)">{{ item.name }}</el-link>
              </el-footer>
            </el-container>
          </el-col>

          <el-col :xs="12" :sm="12" :lg="6">
            <el-container>
              <el-main>
                <vue-hover-mask style="background-color: white" class="vue-hover-mask" @click="dialogVisible = true">
                  <svg-icon icon-class="add" style="color: #bfcbd9;height: 30%;width: 30%;text-align:center;" />
                  <template v-slot:action>
                    <span style="font-size: 20px">添加</span>
                  </template>
                </vue-hover-mask>
                <el-dialog v-if="form.id === 0" title="新增媒体库" :visible.sync="dialogVisible" width="30%">
                  <el-form ref="form" status-icon :rules="rules" :model="form" label-width="100px">
                    <el-input v-show="false" v-model="form.id" />
                    <el-form-item prop="name" label="媒体库名称">
                      <el-input v-model="form.name" placeholder="请输入媒体库名称" />
                    </el-form-item>
                    <el-form-item prop="directory" label="媒体库目录">
                      <el-input v-model="form.directory" placeholder="请输入媒体库目录，多个用'|'隔开" />
                    </el-form-item>
                    <el-form-item label="视频类型">
                      <el-radio-group v-model="form.type">
                        <el-radio :label="0">电影</el-radio>
                        <el-radio :label="1">电视剧</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-form>
                  <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveMediaInfo('form')">确 定</el-button>
                  </span>
                </el-dialog>
                <el-dialog v-else title="更新媒体库" :visible.sync="dialogVisible" width="30%">
                  <el-form ref="form" status-icon :rules="rules" :model="form" label-width="100px">
                    <el-input v-show="false" v-model="form.id" />
                    <el-form-item prop="name" label="媒体库名称">
                      <el-input v-model="form.name" placeholder="请输入媒体库名称" disabled />
                    </el-form-item>
                    <el-form-item prop="directory" label="媒体库目录">
                      <el-input v-model="form.directory" placeholder="请输入媒体库目录，多个用'|'隔开" />
                    </el-form-item>
                    <el-form-item label="视频类型">
                      <el-radio-group v-model="form.type" disabled>
                        <el-radio :label="0">电影</el-radio>
                        <el-radio :label="1">电视剧</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-form>
                  <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveMediaInfo('form')">确 定</el-button>
                  </span>
                </el-dialog>
              </el-main>
            </el-container>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import 'element-ui/lib/theme-chalk/display.css'
import { insertMediaInfo, updateMediaInfo, getMediaInfo, deleteMediaInfo } from '@/api/user'
import VueHoverMask from '@/components/HoverMask/VueHoverMask'
export default {
  name: 'Dashboard',
  components: {
    VueHoverMask
  },
  computed: {
    ...mapGetters([
      'medias'
    ])
  },
  methods: {
    saveMediaInfo: function(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 发起请求
          if (this.$refs[formName].model.id === 0) {
            insertMediaInfo(this.$refs[formName].model).then(response => {
              this.$message({
                type: 'success',
                message: '添加成功!'
              })
              this.$router.go(0)
            })
          } else {
            updateMediaInfo(this.$refs[formName].model).then(response => {
              this.$message({
                type: 'success',
                message: '更新成功!'
              })
              this.$router.go(0)
            })
          }
          this.dialogVisible = false
        } else {
          return false
        }
      })
    },
    handleClick: function(e) {
      this.$router.push({ name: 'media_' + e })
    },
    openUpdateDialog: function(e) {
      getMediaInfo(e).then(response => {
        this.form = response.data
        this.dialogVisible = true
      })
    },
    openDeleteDialog: function(e) {
      this.$confirm('此操作将永久删除该媒体库, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMediaInfo(e).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.$router.go(0)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  },
  // eslint-disable-next-line vue/order-in-components
  data() {
    var validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入媒体库名称'))
      } else {
        callback()
      }
    }
    var validatePath = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入路径信息，多个路径用"|"隔开'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      form: {
        // eslint-disable-next-line no-undef
        id: 0,
        name: '',
        type: 0,
        directory: ''
      },
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        directory: [
          { validator: validatePath, trigger: 'blur' }
        ]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .el-header, .el-footer {
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-main {
    color: #333;
    text-align: center;
    line-height: 160px;
  }
  .el-col {
    border-radius: 4px;
    margin-bottom: 32px;
  }

  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #fb7299;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .vue-hover-mask{
    background: #fb7299;
  }
  .svg-icon-action {
    color: #c0c4cc;
    height: 25%;
    width: 25%;
    text-align:center;
  }
  .svg-icon-action:hover {
    color: #ffffff;
    height: 35%;
    width: 35%;
    text-align:center;
  }
</style>

