-- 创建管理员表
CREATE TABLE admin (
                       id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
                       username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                       password VARCHAR(255) NOT NULL COMMENT '密码哈希',
                       nickname VARCHAR(50) COMMENT '昵称',
                       last_login DATETIME COMMENT '最后登录时间',
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='管理员表';

-- 创建普通用户表
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                      username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                      password VARCHAR(255) NOT NULL COMMENT '密码哈希',
                      nickname VARCHAR(50) NOT NULL COMMENT '昵称',
                      email VARCHAR(100) UNIQUE COMMENT '电子邮件',
                      avatar VARCHAR(255) DEFAULT '/images/avatars/default-avatar.jpg' COMMENT '用户头像URL',
                      status ENUM('active', 'inactive', 'banned') NOT NULL DEFAULT 'active' COMMENT '用户状态',
                      last_login DATETIME COMMENT '最后登录时间',
                      created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='普通用户表';

-- 创建个人资料表
CREATE TABLE profile (
                         id INT AUTO_INCREMENT PRIMARY KEY COMMENT '资料ID',
                         avatar VARCHAR(255) COMMENT '头像URL',
                         title VARCHAR(100) COMMENT '标题/昵称',
                         description TEXT COMMENT '个人描述',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='个人资料表';

-- 创建文章表
CREATE TABLE article (
                         id INT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
                         title VARCHAR(255) NOT NULL COMMENT '文章标题',
                         summary TEXT NOT NULL COMMENT '文章摘要',
                         content LONGTEXT NOT NULL COMMENT '文章内容（HTML格式）',
                         cover_image VARCHAR(255) COMMENT '封面图片URL',
                         publish_date DATE NOT NULL COMMENT '发布日期',
                         tag VARCHAR(50) COMMENT '文章标签',
                         is_pinned BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否置顶',
                         view_count INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='文章表';

-- 创建文章关键词关联表
CREATE TABLE article_keyword (
                                 id INT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
                                 article_id INT NOT NULL COMMENT '文章ID',
                                 keyword VARCHAR(50) NOT NULL COMMENT '关键词',
                                 created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
                                 UNIQUE KEY uk_article_keyword (article_id, keyword) COMMENT '确保一篇文章的关键词不重复'
) COMMENT='文章关键词关联表';

-- 创建评论表
CREATE TABLE comment (
                         id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
                         article_id INT NOT NULL COMMENT '文章ID',
                         parent_id INT COMMENT '父评论ID，用于回复评论',
                         user_id INT COMMENT '关联用户ID，已登录用户',
                         author VARCHAR(50) NOT NULL COMMENT '评论作者名称',
                         avatar VARCHAR(255) COMMENT '作者头像URL',
                         content TEXT NOT NULL COMMENT '评论内容',
                         status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending' COMMENT '评论状态',
                         ip_address VARCHAR(45) COMMENT '评论者IP地址',
                         user_agent TEXT COMMENT '评论者浏览器信息',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
                         FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE SET NULL,
                         FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
) COMMENT='评论表';

-- 创建系统设置表
CREATE TABLE setting (
                         id INT AUTO_INCREMENT PRIMARY KEY COMMENT '设置ID',
                         setting_key VARCHAR(50) NOT NULL UNIQUE COMMENT '设置键名',
                         setting_value TEXT COMMENT '设置值',
                         description VARCHAR(255) COMMENT '设置描述',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='系统设置表';

-- 创建索引
-- 文章表索引
CREATE INDEX idx_article_publish_date ON article(publish_date);
CREATE INDEX idx_article_tag ON article(tag);
CREATE INDEX idx_article_is_pinned ON article(is_pinned);

-- 评论表索引
CREATE INDEX idx_comment_article_id ON comment(article_id);
CREATE INDEX idx_comment_parent_id ON comment(parent_id);
CREATE INDEX idx_comment_status ON comment(status);
CREATE INDEX idx_comment_created_at ON comment(created_at);
CREATE INDEX idx_comment_user_id ON comment(user_id);

-- 用户表索引
CREATE INDEX idx_user_status ON user(status);
CREATE INDEX idx_user_created_at ON user(created_at);
CREATE INDEX idx_user_last_login ON user(last_login);
CREATE INDEX idx_user_email ON user(email);