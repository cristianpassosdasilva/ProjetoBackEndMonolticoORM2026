USE db_ecommerce;

-- ============================================================
-- EXERCÍCIO 1: Inserir produto com validações
-- ============================================================
DROP PROCEDURE IF EXISTS inserirProduto;

DELIMITER $$
CREATE PROCEDURE inserirProduto(
    IN p_nome        VARCHAR(60),
    IN p_categoria   VARCHAR(40),
    IN p_preco       DOUBLE,
    IN p_estoque     SMALLINT,
    IN p_imagem      VARCHAR(100)
)
BEGIN
    DECLARE v_categoriaID INT DEFAULT NULL;

    IF p_categoria IS NULL OR p_categoria = '' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Erro: categoria não informada.';
    END IF;

    SELECT CategoriaID INTO v_categoriaID
    FROM categorias
    WHERE categoria = p_categoria
    LIMIT 1;

    IF v_categoriaID IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Erro: categoria não encontrada na tabela de categorias.';
    END IF;

    IF p_preco <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Erro: o preço deve ser maior que zero.';
    END IF;

    INSERT INTO produtos (ProdutoNome, CategoriaID, preco, UnidadesEmEstoque, Imagem)
    VALUES (p_nome, v_categoriaID, p_preco, p_estoque, p_imagem);

    SELECT CONCAT('Produto "', p_nome, '" inserido com sucesso. ID: ', LAST_INSERT_ID()) AS mensagem;
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 2: Prioridade de atendimento por país (CASE)
-- ============================================================
DROP PROCEDURE IF EXISTS prioridadeAtendimento;

DELIMITER $$
CREATE PROCEDURE prioridadeAtendimento()
BEGIN
    SELECT
        ClienteID,
        nome,
        pais,
        CASE pais
            WHEN 'Brazil'  THEN 'Alta'
            WHEN 'Germany' THEN 'Média'
            ELSE                 'Baixa'
        END AS prioridade_atendimento
    FROM clientes
    ORDER BY
        CASE pais
            WHEN 'Brazil'  THEN 1
            WHEN 'Germany' THEN 2
            ELSE                3
        END,
        nome;
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 3: Inserir cliente único (sem duplicata)
-- ============================================================
DROP PROCEDURE IF EXISTS inserirClienteUnico;

DELIMITER $$
CREATE PROCEDURE inserirClienteUnico(
    IN p_clienteID CHAR(5),
    IN p_nome      VARCHAR(30),
    IN p_cargo     VARCHAR(30),
    IN p_endereco  VARCHAR(60),
    IN p_cidade    VARCHAR(15),
    IN p_cep       VARCHAR(10),
    IN p_pais      VARCHAR(15),
    IN p_telefone  VARCHAR(24),
    IN p_fax       VARCHAR(24)
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    SELECT COUNT(*) INTO v_existe
    FROM clientes
    WHERE ClienteID = p_clienteID;

    IF v_existe > 0 THEN
        SELECT 'Cliente já cadastrado.' AS mensagem;
    ELSE
        INSERT INTO clientes (ClienteID, nome, cargo, endereco, cidade, cep, pais, telefone, Fax)
        VALUES (p_clienteID, p_nome, p_cargo, p_endereco, p_cidade, p_cep, p_pais, p_telefone, p_fax);

        SELECT 'Cliente inserido com sucesso.' AS mensagem;
    END IF;
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 4: Coluna de rastreamento + procedure
-- ============================================================
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'db_ecommerce'
      AND TABLE_NAME   = 'pedidos'
      AND COLUMN_NAME  = 'codigo_rastreamento'
);

SET @alter_sql = IF(@col_exists = 0,
    'ALTER TABLE pedidos ADD COLUMN codigo_rastreamento VARCHAR(20) DEFAULT NULL',
    'SELECT "coluna ja existe" AS info'
);

PREPARE alter_stmt FROM @alter_sql;
EXECUTE alter_stmt;
DEALLOCATE PREPARE alter_stmt;

DROP PROCEDURE IF EXISTS gerarCodigoRastreamento;

DELIMITER $$
CREATE PROCEDURE gerarCodigoRastreamento()
BEGIN
    UPDATE pedidos
    SET codigo_rastreamento = CONCAT('RASTREIO-', LPAD(PedidoID, 6, '0'));

    SELECT CONCAT(ROW_COUNT(), ' pedido(s) atualizado(s) com código de rastreamento.') AS mensagem;
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 5: Atualizar cliente com auditoria
-- ============================================================
CREATE TABLE IF NOT EXISTS log_alteracoes_cliente (
    LogID          INT AUTO_INCREMENT PRIMARY KEY,
    ClienteID      CHAR(5)     NOT NULL,
    campo          VARCHAR(60) NOT NULL,
    valor_antigo   TEXT,
    valor_novo     TEXT,
    data_alteracao DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP PROCEDURE IF EXISTS atualizarClienteComAuditoria;

DELIMITER $$
CREATE PROCEDURE atualizarClienteComAuditoria(
    IN p_clienteID   CHAR(5),
    IN p_campo       VARCHAR(60),
    IN p_valor_novo  TEXT
)
BEGIN
    DECLARE v_valor_antigo TEXT DEFAULT NULL;
    DECLARE v_existe       INT  DEFAULT 0;

    SELECT COUNT(*) INTO v_existe
    FROM clientes
    WHERE ClienteID = p_clienteID;

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Erro: ClienteID não encontrado.';
    END IF;

    SET @sql_select = CONCAT('SELECT `', p_campo, '` INTO @v_antigo FROM clientes WHERE ClienteID = ?');
    SET @p_id = p_clienteID;
    PREPARE stmt_sel FROM @sql_select;
    EXECUTE stmt_sel USING @p_id;
    DEALLOCATE PREPARE stmt_sel;
    SET v_valor_antigo = @v_antigo;

    SET @sql_update = CONCAT('UPDATE clientes SET `', p_campo, '` = ? WHERE ClienteID = ?');
    SET @p_novo = p_valor_novo;
    PREPARE stmt_upd FROM @sql_update;
    EXECUTE stmt_upd USING @p_novo, @p_id;
    DEALLOCATE PREPARE stmt_upd;

    INSERT INTO log_alteracoes_cliente (ClienteID, campo, valor_antigo, valor_novo)
    VALUES (p_clienteID, p_campo, v_valor_antigo, p_valor_novo);

    SELECT CONCAT('Campo "', p_campo, '" do cliente ', p_clienteID,
                  ' atualizado de "', IFNULL(v_valor_antigo, 'NULL'),
                  '" para "', p_valor_novo, '".') AS mensagem;
END$$
DELIMITER ;

SELECT 'Todos os procedures criados com sucesso!' AS status;
