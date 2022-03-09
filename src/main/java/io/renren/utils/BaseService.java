/*
package io.renren.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.ShiroUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

*/
/**
 * @Author: Tom.Min
 * @Date: 2021/7/30 14:19
 * @Desc:
 *//*

@Slf4j
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    private  final  String createTime="createTime";
    private  final  String creatorName="creatorName";
    private  final  String creatorId="creatorId";
    private  final  String updateUserName="updateUserName";
    private  final  String updateUserId="updateUserId";
    private  final  String updateTime="updateTime";
    private  final  String ifDelete="ifDelete";
    private  final  String ifDeleteField="if_delete";
    public final byte deleted=1;
    public final byte undeleted=0;
    @Override
    public boolean updateById(T entity) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        setUpdateMessage(entity,userEntity);
        return super.updateById(entity);
    }

    @Override
    public T getById(Serializable id) {
        QueryWrapper<T> wrapper = constructWrapperAddIfExistFiled(null);

        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        try {
            String keyColumn = tableInfo.getKeyColumn();
            wrapper.eq(keyColumn, id);
        }catch (Exception e){
        }
        T one = getOne(wrapper);
        return  one;
    }




    @Override
    public T getOne(Wrapper<T> queryWrapper) {
        QueryWrapper<T> wrapper = constructWrapperAddIfExistFiled((QueryWrapper<T>) queryWrapper);
        T one = super.getOne(wrapper);
        return one;
    }



    @Override
    public boolean saveBatch(Collection<T> entityList) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        for (T o : entityList) {
            setCreateMessage(o,userEntity);
            setUpdateMessage(o,userEntity);
            setIfDeleteStatue(o,undeleted);
        }
        return super.saveBatch(entityList);
    }

    @Override
    public boolean save(T entity) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        setCreateMessage(entity,userEntity);
        setUpdateMessage(entity,userEntity);
        setIfDeleteStatue(entity,undeleted);
        return super.save(entity);
    }


    @Override
    public boolean removeById(Serializable id) {
        if (ifExistDelFiled()){
            T byId = this.getById(id);
            setIfDeleteStatue(byId,deleted);
            setUpdateMessage(byId,ShiroUtils.getUserEntity());
            return true;
        }
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (ifExistDelFiled()){
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>().set(ifDeleteField, deleted);
            updateWrapper.in(tableInfo.getKeyColumn(),idList);
            update(updateWrapper);
            return true;
        }else {
            return super.removeByIds(idList);
        }
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        setUpdateMessage(entity,userEntity);
        judgeIfUpdateCreateMessage(entity,userEntity);
        try {
            Field id = entity.getClass().getDeclaredField("id");
            id.setAccessible(true);
            Object o = id.get(entity);
            if (Objects.isNull(o)){
                save(entity);
            }else {
                updateById(entity);
            }
            return true;
        } catch (Exception e) {
        }

        return super.saveOrUpdate(entity);
    }

    private boolean ifExistDelFiled(){
        try {
            Field declaredField = currentModelClass().getDeclaredField(ifDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private QueryWrapper<T> constructWrapperAddIfExistFiled(QueryWrapper<T> wrapper) {
        if (Objects.isNull(wrapper)){
            wrapper = new QueryWrapper<>();
        }
        String sqlSegment = wrapper.getExpression().getSqlSegment();
        if (sqlSegment.contains(ifDeleteField)){
            return wrapper;
        }
        try {
            Field declaredField = this.entityClass.getDeclaredField(ifDelete);
            wrapper.ne(ifDeleteField, deleted);
        } catch (Exception e) {
        }
        return wrapper;
    }
    private void judgeIfUpdateCreateMessage(T entity,SysUserEntity userEntity) {
        Class<?> aClass = entity.getClass();
        try {
            Field declaredField = aClass.getDeclaredField(this.createTime);
            declaredField.setAccessible(true);
            Object o = declaredField.get(entity);
            if (Objects.isNull(o)){
                setCreateMessage(entity,userEntity);
            }
        } catch (Exception e) {
            return;
        }
    }


    private void setIfDeleteStatue(T entity,byte deleteStatus) {
        try {
            Class<?> aClass = entity.getClass();
            Field createTime = aClass.getDeclaredField(this.ifDelete);
            createTime.setAccessible(true);
            if (Objects.isNull(createTime.get(entity))){
                createTime.set(entity, deleteStatus);
            }
        } catch (Exception e) {
        }

    }

    private void setCreateMessage(T entity, SysUserEntity userEntity) {
        Class aClass=entity.getClass();

        String username = userEntity.getUsername();
        Long userId = userEntity.getUserId();
        try {
            Field creatorName = aClass.getDeclaredField(this.creatorName);
            creatorName.setAccessible(true);
            creatorName.set(entity, username);
        } catch (Exception e) {

        }
        try {
            Field creatorId = aClass.getDeclaredField(this.creatorId);
            creatorId.setAccessible(true);
            creatorId.set(entity, userId);
        } catch (Exception e) {

        }


        try {
            Field createTime = aClass.getDeclaredField(this.createTime);
            createTime.setAccessible(true);
            createTime.set(entity, new Date());
        } catch (Exception e) {

        }
    }

    private void setUpdateMessage(T entity,SysUserEntity userEntity) {
        String username = userEntity.getUsername();
        Long userId = userEntity.getUserId();
        Class aClass=entity.getClass();
        try {
            Field createTime = aClass.getDeclaredField(this.updateTime);
            createTime.setAccessible(true);
            createTime.set(entity, new Date());
        } catch (Exception e) {
        }
        try {
            Field updateUserName = aClass.getDeclaredField(this.updateUserName);
            updateUserName.setAccessible(true);
            updateUserName.set(entity, username);
        } catch (Exception e) {
        }


        try {
            Field updateUserId = aClass.getDeclaredField(this.updateUserId);
            updateUserId.setAccessible(true);
            updateUserId.set(entity, userId);
        } catch (Exception e) {

        }

    }
}
*/
