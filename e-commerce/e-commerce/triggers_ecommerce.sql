USE db_ecommerce;

-- ============================================================
-- EXERCÍCIO 1: Tabela de histórico + trigger de exclusão (log)
-- ============================================================
CREATE TABLE IF NOT EXISTS historico_produtos_excluidos (
    HistoricoID    INT AUTO_INCREMENT PRIMARY KEY,
    ProdutoID      INT           NOT NULL,
    ProdutoNome    VARCHAR(60),
    CategoriaID    INT,
    preco          DOUBLE(10,2),
    UnidadesEmEstoque SMALLINT,
    Imagem         VARCHAR(100),
    data_exclusao  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TRIGGER IF EXISTS trg_historico_produto_excluido;

DELIMITER $$
CREATE TRIGGER trg_historico_produto_excluido
AFTER DELETE ON produtos
FOR EACH ROW
BEGIN
    INSERT INTO historico_produtos_excluidos
        (ProdutoID, ProdutoNome, CategoriaID, preco, UnidadesEmEstoque, Imagem)
    VALUES
        (OLD.ProdutoID, OLD.ProdutoNome, OLD.CategoriaID, OLD.preco, OLD.UnidadesEmEstoque, OLD.Imagem);
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 2: Tabela produto_backup + trigger de backup
-- ============================================================
CREATE TABLE IF NOT EXISTS produto_backup (
    BackupID       INT AUTO_INCREMENT PRIMARY KEY,
    ProdutoID      INT           NOT NULL,
    ProdutoNome    VARCHAR(60),
    CategoriaID    INT,
    preco          DOUBLE(10,2),
    UnidadesEmEstoque SMALLINT,
    Imagem         VARCHAR(100),
    data_backup    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TRIGGER IF EXISTS trg_backup_produto_excluido;

DELIMITER $$
CREATE TRIGGER trg_backup_produto_excluido
BEFORE DELETE ON produtos
FOR EACH ROW
BEGIN
    INSERT INTO produto_backup
        (ProdutoID, ProdutoNome, CategoriaID, preco, UnidadesEmEstoque, Imagem)
    VALUES
        (OLD.ProdutoID, OLD.ProdutoNome, OLD.CategoriaID, OLD.preco, OLD.UnidadesEmEstoque, OLD.Imagem);
END$$
DELIMITER ;

-- ============================================================
-- EXERCÍCIO 3: Trigger que valida desconto no detalhe do pedido
-- ============================================================
DROP TRIGGER IF EXISTS trg_validar_desconto;

DELIMITER $$
CREATE TRIGGER trg_validar_desconto
BEFORE INSERT ON detalhes_pedido
FOR EACH ROW
BEGIN
    IF NEW.desconto > NEW.precoVenda THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Erro: o desconto não pode ser maior que o preço de venda do item.';
    END IF;
END$$
DELIMITER ;

SELECT 'Todas as triggers criadas com sucesso!' AS status;
