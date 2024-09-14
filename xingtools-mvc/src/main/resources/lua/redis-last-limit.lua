-- 获取 key
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local expire = tonumber(ARGV[2])
local debounce = tonumber(ARGV[3]) or 0 -- 默认仅支持节流


local currentLimit = tonumber(redis.call('get', key) or "0")

if currentLimit + 1 > limit then
    return 0
else
    -- 自增长 1
    redis.call("INCRBY", key, 1)
    -- 设置过期时间
    if debounce > 0 or currentLimit + 1 == 1 then
        redis.call("EXPIRE", key, expire)
    end
    return currentLimit + 1
end